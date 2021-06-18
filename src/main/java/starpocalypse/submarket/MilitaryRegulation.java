package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class MilitaryRegulation extends SubmarketChanger {

    private final SimpleSet allowedFactions = new SimpleSet("faction", "militaryRegulationFaction.csv");
    private final SimpleSet allowedSubmarkets = new SimpleSet("submarket", "militaryRegulationSubmarket.csv");

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
        return stack.isWeaponStack() || stack.isFighterWingStack();
    }

    protected boolean isInvalid(FleetMemberAPI fleetMember) {
        return !fleetMember.getVariant().hasHullMod(HullMods.CIVGRADE);
    }

    private void addToMilitary(CargoStackAPI stack) {
        if (militaryMarket == null) {
            log.info("Removing " + stack.getDisplayName());
        } else {
            log.info("Moving to military " + stack.getDisplayName());
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
