package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import org.json.JSONObject;
import starpocalypse.helper.ConfigUtils;
import starpocalypse.market.IndustryAdder;
import starpocalypse.market.MarketListener;
import starpocalypse.market.StationAdder;
import starpocalypse.reputation.EngagementListener;
import starpocalypse.reputation.RaidListener;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.SubmarketSwapper;

public class StarpocalypseMod extends BaseModPlugin {

    private static JSONObject settings;

    @Override
    public void onApplicationLoad() throws Exception {
        settings = Global.getSettings().loadJSON("starpocalypse.json");
        ConfigUtils.init(settings);
    }

    @Override
    public void onNewGameAfterTimePass() {
        addDmodsToStartingFleet();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        if (settings.optBoolean("addDmodsToShipsInSubmarkets", true)) {
            ShipDamager.register();
        }
        if (settings.optBoolean("militaryRegulation", true)) {
            SubmarketSwapper.register();
        }
        industryChanges();
        combatAdjustedReputation();
        hostilityForSpecialItemRaid();
    }

    private void industryChanges() {
        MarketListener listener = new MarketListener();
        addGroundDefenses(listener);
        addPatrolHq(listener);
        addStations(listener);
        listener.register();
    }

    private void addDmodsToStartingFleet() {
        if (settings.optBoolean("addDmodsToStartingFleet", true)) {
            StartingFleetDamager.apply();
        }
    }

    private void addGroundDefenses(MarketListener listener) {
        if (settings.optBoolean("addGroundDefenses", true)) {
            listener.add(
                new IndustryAdder(Industries.GROUNDDEFENSES, true, Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES)
            );
        }
    }

    private void addStations(MarketListener listener) {
        if (settings.optBoolean("addStations", true)) {
            listener.add(new StationAdder());
        }
    }

    private void addPatrolHq(MarketListener listener) {
        if (settings.optBoolean("addPatrolHq", true)) {
            listener.add(
                new IndustryAdder(
                    Industries.PATROLHQ,
                    false,
                    Industries.PATROLHQ,
                    Industries.MILITARYBASE,
                    Industries.HIGHCOMMAND
                )
            );
        }
    }

    private void hostilityForSpecialItemRaid() {
        if (settings.optBoolean("hostilityForSpecialItemRaid", true)) {
            RaidListener.register();
        }
    }

    private void combatAdjustedReputation() {
        if (settings.optBoolean("combatAdjustedReputation", true)) {
            EngagementListener.register();
        }
    }
}
