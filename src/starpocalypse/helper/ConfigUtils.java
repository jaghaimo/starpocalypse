package starpocalypse.helper;

import lombok.Getter;
import org.json.JSONObject;
import starpocalypse.config.SimpleSet;

public class ConfigUtils {

    @Getter
    private static int minDmods;

    @Getter
    private static int maxDmods;

    @Getter
    private static final SimpleSet regulatedFaction = new SimpleSet("faction", "militaryRegulationFaction.csv");

    @Getter
    private static final SimpleSet shipDamageFaction = new SimpleSet("faction", "shipDamageFaction.csv");

    @Getter
    private static final SimpleSet shipDamageSubmarket = new SimpleSet("submarket", "shipDamageSubmarket.csv");

    public static void init(JSONObject settings) {
        minDmods = settings.optInt("minimumDmods", 2);
        maxDmods = settings.optInt("maximumDmods", 4);
    }
}
