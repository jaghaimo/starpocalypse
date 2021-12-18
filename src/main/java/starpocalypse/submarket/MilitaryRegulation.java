package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleMap;
import starpocalypse.config.SimpleSet;

@Log4j
public class MilitaryRegulation extends SubmarketChanger {

    private final SimpleSet allowedFactions = new SimpleSet("faction", "militaryRegulationFaction.csv");
    private final SimpleSet allowedSubmarkets = new SimpleSet("submarket", "militaryRegulationSubmarket.csv");
    private final SimpleMap stabilityCargoValues = new SimpleMap("stability", "cargo", "militaryContraband.csv");
    private final SimpleMap stabilityShipValues = new SimpleMap("stability", "ship", "militaryContraband.csv");
    private final SimpleSet contrabandWhitelist = new SimpleSet("whitelist", "contrabandWhitelist.csv");
    private final SimpleSet contrabandBlacklist = new SimpleSet("blacklist", "contrabandBlacklist.csv");

    private SubmarketAPI militaryMarket;
    protected int stability;
    protected String stabilityKey;

    @Override
    protected void init(SubmarketAPI submarket) {
        militaryMarket = getSubmarket(submarket, Submarkets.GENERIC_MILITARY);
        stability = (int) submarket.getMarket().getStabilityValue();
        stabilityKey = String.valueOf(stability);
    }

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack) {
        if (!isInvalid(stack) || isWhitelisted(stack) || isAllowed(submarket, stack)) {
            return;
        }
        cargo.removeStack(stack);
        removeOrAddToMilitary(stack);
    }

    @Override
    protected void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship) {
        if (!isInvalid(ship) || isWhitelisted(ship) || isAllowed(submarket, ship)) {
            return;
        }
        ships.removeFleetMember(ship);
        removeOrAddToMilitary(ship);
    }

    protected boolean isInvalid(CargoStackAPI stack) {
        return isBlacklisted(stack) || stack.isModSpecStack() || stack.isWeaponStack() || stack.isFighterWingStack();
    }

    protected boolean isInvalid(FleetMemberAPI fleetMember) {
        return isBlacklisted(fleetMember) || !fleetMember.getVariant().hasHullMod(HullMods.CIVGRADE);
    }

    protected boolean isAllowed(SubmarketAPI submarket, CargoStackAPI stack) {
        return isAllowed(stabilityCargoValues, stack.getBaseValuePerUnit());
    }

    protected boolean isAllowed(SubmarketAPI submarket, FleetMemberAPI ship) {
        return isAllowed(stabilityShipValues, ship.getBaseValue());
    }

    protected boolean isAllowed(SimpleMap stabilityMap, float value) {
        if (stability <= 0) {
            return true;
        }
        if (stability >= 10) {
            return false;
        }
        float stabilityValue = Float.parseFloat(stabilityMap.get(stabilityKey));
        return value < stabilityValue;
    }

    protected boolean isWhitelisted(CargoStackAPI stack) {
        return contrabandWhitelist.has(stack.getDisplayName());
    }

    protected boolean isWhitelisted(FleetMemberAPI ship) {
        return contrabandWhitelist.has(ship.getHullSpec().getBaseHullId());
    }

    protected boolean isBlacklisted(CargoStackAPI stack) {
        return contrabandBlacklist.has(stack.getDisplayName());
    }

    protected boolean isBlacklisted(FleetMemberAPI ship) {
        return contrabandBlacklist.has(ship.getHullSpec().getBaseHullId());
    }

    private void removeOrAddToMilitary(CargoStackAPI stack) {
        if (militaryMarket == null) {
            log.info("Removing " + stack.getDisplayName());
        } else {
            log.info("Moving to military " + stack.getDisplayName());
            militaryMarket.getCargo().addFromStack(stack);
        }
    }

    private void removeOrAddToMilitary(FleetMemberAPI ship) {
        String shipHullName = ship.getHullSpec().getHullName();
        if (militaryMarket == null) {
            log.info("Removing " + shipHullName);
        } else {
            log.info("Moving to military " + shipHullName);
            militaryMarket.getCargo().getMothballedShips().addFleetMember(ship);
        }
    }

}
