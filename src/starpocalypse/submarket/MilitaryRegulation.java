package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class MilitaryRegulation extends SubmarketChanger {

    private final SimpleSet allowedFactions = new SimpleSet("faction", "militaryRegulationFaction.csv");
    private final SimpleSet allowedSubmarkets = new SimpleSet("submarket", "militaryRegulationSubmarket.csv");
    private final SimpleSet blacklist = new SimpleSet("idOrName", "militaryRegulationBlacklist.csv");

    private SubmarketAPI militaryMarket;

    @Override
    protected void init(SubmarketAPI submarket) {
        militaryMarket = getSubmarket(submarket, Submarkets.GENERIC_MILITARY);
    }

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack) {
        if (!isInvalid(stack)) {
            return;
        }
        cargo.removeStack(stack);
        addToMilitary(stack);
    }

    @Override
    protected void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship) {
        if (!isInvalid(ship)) {
            return;
        }
        ships.removeFleetMember(ship);
        addToMilitary(ship);
    }

    protected boolean isInvalid(CargoStackAPI stack) {
        if (isBlacklisted(blacklist, stack)) {
            return false;
        }
        return stack.isModSpecStack() || stack.isWeaponStack() || stack.isFighterWingStack();
    }

    protected boolean isInvalid(FleetMemberAPI fleetMember) {
        if (isBlacklisted(blacklist, fleetMember)) {
            return false;
        }
        return !(
            fleetMember.getVariant().hasHullMod(HullMods.CIVGRADE) ||
            fleetMember.getVariant().getHints().contains(ShipTypeHints.CIVILIAN)
        );
    }

    protected boolean isBlacklisted(SimpleSet blacklist, CargoStackAPI stack) {
        return blacklist.has(stack.getDisplayName());
    }

    protected boolean isBlacklisted(SimpleSet blacklist, FleetMemberAPI ship) {
        return blacklist.has(ship.getHullSpec().getBaseHullId());
    }

    private void addToMilitary(CargoStackAPI stack) {
        String stackName = stack.getDisplayName();
        if (militaryMarket == null) {
            log.info("Removing " + stackName);
        } else {
            log.info("Moving to military " + stackName);
            militaryMarket.getCargo().addFromStack(stack);
        }
    }

    private void addToMilitary(FleetMemberAPI ship) {
        String shipHullName = ship.getHullSpec().getHullName();
        if (militaryMarket == null) {
            log.info("Removing " + shipHullName);
        } else {
            log.info("Moving to military " + shipHullName);
            militaryMarket.getCargo().getMothballedShips().addFleetMember(ship);
        }
    }
}
