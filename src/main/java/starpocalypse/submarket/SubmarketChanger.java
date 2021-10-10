package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public abstract class SubmarketChanger {

    public void change(SubmarketAPI submarket) {
        init(submarket);
        if (canChange(submarket)) {
            CargoAPI cargo = submarket.getCargo();
            for (CargoStackAPI stack : cargo.getStacksCopy()) {
                changeCargo(submarket, cargo, stack);
            }
            cargo.sort();
            FleetDataAPI ships = cargo.getMothballedShips();
            for (FleetMemberAPI ship : ships.getMembersListCopy()) {
                changeShips(submarket, ships, ship);
            }
            ships.sort();
        }
    }

    protected abstract boolean canChange(SubmarketAPI submarket);

    protected abstract void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack);

    protected abstract void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship);

    protected abstract void init(SubmarketAPI submarket);

    protected boolean acceptFaction(SubmarketAPI submarket, SimpleSet allowedFactions) {
        String factionId = submarket.getMarket().getFactionId();
        if (allowedFactions.has(factionId)) {
            return true;
        }
        log.warn("Skipping unknown faction " + factionId);
        return false;
    }

    protected boolean acceptSubmarket(SubmarketAPI submarket, SimpleSet allowedSubmarkets) {
        String submarketId = submarket.getSpecId();
        if (allowedSubmarkets.has(submarketId)) {
            return true;
        }
        log.warn("Skipping unknown submarket " + submarketId);
        return false;
    }

    protected SubmarketAPI getSubmarket(SubmarketAPI submarket, String submarketSpecId) {
        return submarket.getMarket().getSubmarket(submarketSpecId);
    }
}
