package starpocalypse;

import com.fs.starfarer.api.Global;
import lombok.extern.log4j.Log4j;
import starpocalypse.reputation.EngagementListener;

@Log4j
public class ReputationModule {

    private static final boolean hasReputation = Global.getSettings().getBoolean("starpocalypseReputationModule");

    public static void init() {
        if (hasReputation) {
            log.info("Enabling reputation module");
            new EngagementListener();
        }
    }
}
