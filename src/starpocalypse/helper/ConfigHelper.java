package starpocalypse.helper;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starpocalypse.config.SimpleMap;
import starpocalypse.config.SimpleSet;

public class ConfigHelper {

    @Getter
    private static float blackMarketFenceCut = 0.5f;

    @Getter
    private static int minDmods = 2;

    @Getter
    private static int maxDmods = 4;

    private static boolean regulation = true;

    @Getter
    private static float regulationMaxTier = 0;

    @Getter
    private static float regulationMaxFP = 0;

    private static final SimpleSet regulationFaction = new SimpleSet("faction", "militaryRegulationFaction.csv");

    @Getter
    private static final SimpleSet regulationLegal = new SimpleSet("name", "militaryRegulationLegal.csv");

    @Getter
    private static final SimpleMap regulationStabilityItem = new SimpleMap(
        "stability",
        "item",
        "militaryRegulationStability.csv"
    );

    @Getter
    private static final SimpleMap regulationStabilityShip = new SimpleMap(
        "stability",
        "ship",
        "militaryRegulationStability.csv"
    );

    @Getter
    private static boolean removeEndgameCargo = true;

    @Getter
    private static boolean removeEndgameShips = true;

    @Getter
    private static boolean shyBlackMarket = false;

    @Getter
    private static final SimpleSet shyBlackMarketFaction = new SimpleSet("faction", "shyBlackMarketFaction.csv");

    @Getter
    private static final SimpleSet shipDamageFaction = new SimpleSet("faction", "shipDamageFaction.csv");

    @Getter
    private static final SimpleSet shipDamageSubmarket = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    public static void init(JSONObject settings, Logger log) {
        loadConfig(settings);
        transparentMarket(settings, log);
    }

    public static boolean isUninstall() {
        JSONObject settings = Global.getSettings().getSettingsJSON();
        return !settings.optBoolean("hasStarpocalypse", false);
    }

    public static boolean wantsRegulation(String factionId) {
        return regulation && regulationFaction.has(factionId);
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private static int clamp(int value, int min, int max) {
        value = Math.max(value, min);
        value = Math.min(value, max);
        return value;
    }

    private static void loadConfig(JSONObject settings) {
        blackMarketFenceCut = (float) settings.optDouble("blackMarketFenceCut", 0.5);
        minDmods = clamp(settings.optInt("minimumDmods", 2), 1, 5);
        maxDmods = clamp(settings.optInt("maximumDmods", 4), minDmods, 5);
        regulation = settings.optBoolean("militaryRegulations", true);
        regulationMaxFP = settings.optInt("regulationMaxLegalFP", 0);
        regulationMaxTier = settings.optInt("regulationMaxLegalTier", 0);
        removeEndgameCargo = settings.optBoolean("removeMilitaryEndgameCargo", true);
        removeEndgameShips = settings.optBoolean("removeMilitaryEndgameShips", true);
        shyBlackMarket = settings.optBoolean("shyBlackMarket", true);
    }

    private static void transparentMarket(JSONObject settings, Logger log) {
        if (settings.optBoolean("transparentMarket", true)) {
            float mult = (float) settings.optDouble("transparentMarketMult", 0.5);
            log.info("Setting transponder off market awareness mult to " + mult);
            Global.getSettings().setFloat("transponderOffMarketAwarenessMult", mult);
        }
    }
}
