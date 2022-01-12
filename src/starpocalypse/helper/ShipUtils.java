package starpocalypse.helper;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import java.util.Random;
import lombok.extern.log4j.Log4j;

@Log4j
public class ShipUtils {

    public static void damageShip(FleetMemberAPI ship, int minDmods, int maxDmods) {
        String hullName = ship.getHullSpec().getHullName();
        ShipVariantAPI variant = ship.getVariant();
        Random random = new Random();
        if (DModManager.setDHull(variant)) {
            log.info("Damaging " + hullName);
            int numberOfDmods = random.nextInt(maxDmods - minDmods) + minDmods;
            DModManager.addDMods(variant, true, numberOfDmods, random);
        }
    }
}
