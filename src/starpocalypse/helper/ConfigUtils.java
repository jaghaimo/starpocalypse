package starpocalypse.helper;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starpocalypse.config.SimpleMap;
import starpocalypse.config.SimpleSet;

public class ConfigUtils {

    @Getter
    private static float blackMarketFenceCut;

    @Getter
    private static int minDmods = 2;

    @Getter
    private static int maxDmods = 4;

    @Getter
    private static boolean shyBlackMarket = false;

    @Getter
    private static final SimpleMap regulatedStabilityItem = new SimpleMap(
        "stability",
        "item",
        "militaryRegulationStability.csv"
    );

    @Getter
    private static final SimpleMap regulatedStabilityShip = new SimpleMap(
        "stability",
        "ship",
        "militaryRegulationStability.csv"
    );

    @Getter
    private static final SimpleSet regulatedLegal = new SimpleSet("name", "militaryRegulationLegal.csv");

    @Getter
    private static final SimpleSet regulatedFaction = new SimpleSet("faction", "militaryRegulationFaction.csv");

    @Getter
    private static final SimpleSet shyBlackMarketFaction = new SimpleSet("faction", "shyBlackMarketFaction.csv");

    @Getter
    private static final SimpleSet shipDamageFaction = new SimpleSet("faction", "shipDamageFaction.csv");

    @Getter
    private static final SimpleSet shipDamageSubmarket = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    public static void init(JSONObject settings, Logger log) {
        blackMarketFenceCut = (float) settings.optDouble("blackMarketFenceCut", 0.5);
        minDmods = clamp(settings.optInt("minimumDmods", 2), 1, 5);
        maxDmods = clamp(settings.optInt("maximumDmods", 4), minDmods, 5);
        shyBlackMarket = settings.optBoolean("shyBlackMarket", false);
        if (settings.optBoolean("transparentMarket", true)) {
            float mult = (float) settings.optDouble("transparentMarketMult", 0.5);
            log.info("Setting transponder off market awareness mult to " + mult);
            Global.getSettings().setFloat("transponderOffMarketAwarenessMult", mult);
        }
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private static int clamp(int value, int min, int max) {
        value = Math.max(value, min);
        value = Math.min(value, max);
        return value;
    }
}
