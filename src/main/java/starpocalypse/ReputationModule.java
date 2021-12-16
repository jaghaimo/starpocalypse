package starpocalypse;

import com.fs.starfarer.api.Global;
import lombok.extern.log4j.Log4j;
import starpocalypse.reputation.EngagementListener;

@Log4j
public class ReputationModule {

    public static void init() {
        boolean hasReputation = Global.getSettings().getBoolean("starpocalypseReputationModule");
        if (hasReputation) {
            log.info("Enabling reputation module");
            new EngagementListener();
        }
    }
}
