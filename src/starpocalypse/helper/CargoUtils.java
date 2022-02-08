package starpocalypse.helper;

import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import java.util.Random;
import lombok.extern.log4j.Log4j;

/**
 * Cargo as defined by Starsector - cargo stacks and mothballed fleet
 */
@Log4j
public class CargoUtils {

    public static void damageShip(String location, FleetMemberAPI ship, int minDmods, int maxDmods) {
        String hullName = ship.getHullSpec().getHullName();
        ShipVariantAPI variant = ship.getVariant();
        Random random = new Random();
        if (DModManager.setDHull(variant)) {
            log.info(location + ": Damaging " + hullName);
            int numberOfDmods = getNumberOfDmods(random, minDmods, maxDmods);
            DModManager.addDMods(variant, true, numberOfDmods, random);
        }
    }

    public static int getTier(CargoStackAPI stack) {
        int tier = 0;
        if (stack.isWeaponStack()) {
            WeaponSpecAPI spec = stack.getWeaponSpecIfWeapon();
            tier = spec.getTier();
        } else if (stack.isModSpecStack()) {
            HullModSpecAPI spec = stack.getHullModSpecIfHullMod();
            tier = spec.getTier();
        } else if (stack.isFighterWingStack()) {
            FighterWingSpecAPI spec = stack.getFighterWingSpecIfWing();
            tier = spec.getTier();
        }
        return tier;
    }

    private static int getNumberOfDmods(Random random, int minDmods, int maxDmods) {
        int numDmods = 0;
        if (maxDmods > minDmods) {
            numDmods += random.nextInt(maxDmods - minDmods);
        }
        return numDmods + minDmods;
    }
}
