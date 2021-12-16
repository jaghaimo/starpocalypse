package starpocalypse;

import com.fs.starfarer.api.Global;
import lombok.extern.log4j.Log4j;
import starpocalypse.reputation.EngagementListener;

@Log4j
public class ReputationModule {

    public static void init() {
        boolean hasEngagement = Global.getSettings().getBoolean("starpocalypseReputationModule");
        if (hasEngagement) {
            log.info("Enabling combat module");
            new EngagementListener();
        }
    }
}
