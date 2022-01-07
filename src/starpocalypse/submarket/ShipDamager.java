package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
@RequiredArgsConstructor
public class ShipDamager extends SubmarketChanger {

    private final int minDmods;
    private final int maxDmods;
    private final SimpleSet allowedFactions = new SimpleSet("faction", "shipDamageFaction.csv");
    private final SimpleSet allowedSubmarkets = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    @Override
    protected void init(SubmarketAPI submarket) {}

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack) {}

    @Override
    protected void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship) {
        String hullName = ship.getHullSpec().getHullName();
        ShipVariantAPI variant = ship.getVariant();
        if (DModManager.setDHull(variant)) {
            log.info("Damaging " + hullName);
            addDmods(variant, minDmods, maxDmods);
        }
    }

    protected void addDmods(ShipVariantAPI variant, int minDmods, int maxDmods) {
        Random random = new Random();
        int numberOfDmods = random.nextInt(maxDmods - minDmods) + minDmods;
        DModManager.addDMods(variant, true, numberOfDmods, new Random());
    }
}
