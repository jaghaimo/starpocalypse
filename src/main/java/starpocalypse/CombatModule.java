package starpocalypse;

import com.fs.starfarer.api.Global;

import lombok.extern.log4j.Log4j;
import starpocalypse.combat.EngagementListener;

@Log4j
public class CombatModule {

    public static void init() {
        if (Global.getSettings().getBoolean("starpocalypseEngagementModule")) {
            log.info("Enabling combat module");
            new EngagementListener();
        }
    }
}