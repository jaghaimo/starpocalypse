package starpocalypse;

import lombok.extern.log4j.Log4j;
import starpocalypse.combat.EngagementListener;

@Log4j
public class EngagementModule {

    public static void init(boolean isEnabled) {
        if (isEnabled) {
            log.info("Enabling combat module");
            new EngagementListener();
        }
    }
}
