package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.util.Misc;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class EngagementListener extends BaseCampaignEventListener {

    // At most increases rep by 0.01 when faction hates the one you've beaten
    // (beaten reputation at adjusted faction is -1.0). Negative sign to give
    // a positive increase to the player.
    public static float ADJUSTMENT_FACTOR = -100f;
    // Commissioned faction relationship changes faster. Mind your manners.
    public static float COMMISSIONED_MULT = 3f;
    // Minimal absolute adjustment to consider.
    public static float MIN_ADJUSTMENT = 0.001f;

    public SimpleSet factionBlacklist = new SimpleSet("faction", "engagementBlacklist.csv");

    public EngagementListener() {
        super(false);
        Global.getSector().addTransientListener(this);
    }

    @Override
    public void reportPlayerEngagement(EngagementResultAPI result) {
        if (!result.didPlayerWin()) {
            log.debug("Skipping since player did not win");
            return;
        }
        FactionAPI beatenFaction = result.getLoserResult().getFleet().getFaction();
        log.debug("Trying to adjust rep due to beating " + beatenFaction.getId());
        adjustReputation(beatenFaction);
    }

    private void adjustReputation(FactionAPI beatenFaction) {
        for (FactionAPI consideredFaction : Global.getSector().getAllFactions()) {
            if (!canAdjust(consideredFaction, beatenFaction)) {
                continue;
            }
            adjustReputation(beatenFaction, consideredFaction);
        }
    }

    private void adjustReputation(FactionAPI beatenFaction, FactionAPI consideredFaction) {
        float delta = getAdjustmentDelta(consideredFaction, beatenFaction);
        if (Math.abs(delta) < MIN_ADJUSTMENT) {
            log.debug("Skipping " + consideredFaction.getId() + " due to small rep change");
            return;
        }
        FactionAPI playerFaction = Global.getSector().getPlayerFaction();
        log.info("Adjusting " + consideredFaction.getId() + " rep by " + String.valueOf(delta));
        playerFaction.adjustRelationship(consideredFaction.getId(), delta);
    }

    private boolean canAdjust(FactionAPI faction, FactionAPI beatenFaction) {
        if (isBlacklisted(faction)) {
            log.debug("Skipping blacklisted " + faction.getId());
            return false;
        }
        if (isFaction(faction, beatenFaction)) {
            log.debug("Skipping involved " + faction.getId());
            return false;
        }
        return true;
    }

    private float getAdjustmentDelta(FactionAPI consideredFaction, FactionAPI beatenFaction) {
        float delta = consideredFaction.getRelationship(beatenFaction.getId()) / ADJUSTMENT_FACTOR;
        FactionAPI commissionedFaction = Misc.getCommissionFaction();
        if (isFaction(consideredFaction, commissionedFaction)) {
            log.debug("Commissioned rep increase for " + consideredFaction.getId());
            delta *= COMMISSIONED_MULT;
        }
        return delta;
    }

    private boolean isBlacklisted(FactionAPI faction) {
        return factionBlacklist.has(faction.getId());
    }

    private boolean isFaction(FactionAPI factionA, FactionAPI factionB) {
        return factionA.equals(factionB);
    }
}