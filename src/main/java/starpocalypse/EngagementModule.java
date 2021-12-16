package starpocalypse;

import com.fs.starfarer.api.Global;
import lombok.extern.log4j.Log4j;
import starpocalypse.combat.EngagementListener;

@Log4j
public class EngagementModule {

    public static void init() {
        boolean hasEngagement = Global.getSettings().getBoolean("starpocalypseEngagementModule");
        if (hasEngagement) {
            log.info("Enabling combat module");
            new EngagementListener();
        }
    }
}
