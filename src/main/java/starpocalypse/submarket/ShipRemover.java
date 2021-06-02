package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class ShipRemover extends SubmarketChanger {

    private SimpleSet allowedFactions = new SimpleSet("faction", "shipRemoveFaction.csv");
    private SimpleSet allowedSubmarkets = new SimpleSet("submarket", "shipRemoveSubmarket.csv");

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeImpl(SubmarketAPI submarket) {
        CargoAPI cargo = submarket.getCargo();
        FleetDataAPI ships = cargo.getMothballedShips();
        for (FleetMemberAPI ship : ships.getMembersListCopy()) {
            clearShip(ships, ship);
        }
    }

    private void clearShip(FleetDataAPI ships, FleetMemberAPI ship) {
        if (isInvalid(ship)) {
            log.info("Removing " + ship.getHullSpec().getHullName());
            ships.removeFleetMember(ship);
        }
    }

    private boolean isInvalid(FleetMemberAPI fleetMember) {
        return !fleetMember.getVariant().hasHullMod(HullMods.CIVGRADE);
    }
}
