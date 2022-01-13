package starpocalypse.helper;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import org.json.JSONObject;
import starpocalypse.config.SimpleSet;

public class ConfigUtils {

    @Getter
    private static int minDmods;

    @Getter
    private static int maxDmods;

    @Getter
    private static boolean shyBlackMarket;

    @Getter
    private static final SimpleSet regulatedFaction = new SimpleSet("faction", "militaryRegulationFaction.csv");

    @Getter
    private static final SimpleSet shipDamageFaction = new SimpleSet("faction", "shipDamageFaction.csv");

    @Getter
    private static final SimpleSet shipDamageSubmarket = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    public static void init(JSONObject settings) {
        minDmods = clamp(settings.optInt("minimumDmods", 2), 1, 5);
        maxDmods = clamp(settings.optInt("maximumDmods", 4), minDmods, 5);
        shyBlackMarket = settings.optBoolean("shyBlackMarket", false);
        if (settings.optBoolean("transparentBlackMarket", true)) {
            float mult = (float) settings.optDouble("transparentBlackMarketMult", 0.5);
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
