package starpocalypse.submarket;

import java.util.Random;

import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class ShipDamager extends SubmarketChanger {

    private SimpleSet allowedFactions = new SimpleSet("faction", "shipDamageFaction.csv");
    private SimpleSet allowedSubmarkets = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeImpl(SubmarketAPI submarket) {
        FleetDataAPI ships = submarket.getCargo().getMothballedShips();
        for (FleetMemberAPI ship : ships.getMembersListCopy()) {
            damageShip(ship);
        }
    }

    private void damageShip(FleetMemberAPI ship) {
        String hullName = ship.getHullSpec().getHullName();
        ShipVariantAPI variant = ship.getVariant();
        if (DModManager.setDHull(variant)) {
            log.info("Damaging " + hullName);
            DModManager.addDMods(variant, true, 1, new Random());
        }
    }
}
