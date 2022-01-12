package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import java.util.List;
import java.util.Random;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;

@Log4j
public class StartingFleetDamager {

    private static int minDmods;
    private static int maxDmods;

    public static void apply(JSONObject settings) {
        minDmods = settings.optInt("minimumDmods", 2);
        maxDmods = settings.optInt("maximumDmods", 4);
        log.info(String.format("Damaging starting fleet with %d to %d d-mods", minDmods, maxDmods));
        List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (FleetMemberAPI member : members) {
            damageShip(member);
        }
    }

    public static void damageShip(FleetMemberAPI ship) {
        String hullName = ship.getHullSpec().getHullName();
        ShipVariantAPI variant = ship.getVariant();
        if (DModManager.setDHull(variant)) {
            log.info("Damaging " + hullName);
            Random random = new Random();
            int numberOfDmods = random.nextInt(maxDmods - minDmods) + minDmods;
            DModManager.addDMods(variant, true, numberOfDmods, random);
        }
    }
}
