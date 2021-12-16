package starpocalypse;

import com.fs.starfarer.api.Global;
import lombok.extern.log4j.Log4j;
import starpocalypse.submarket.MilitaryContraband;
import starpocalypse.submarket.MilitaryRegulation;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.ShyBlackMarketListener;
import starpocalypse.submarket.SubmarketChanger;
import starpocalypse.submarket.SubmarketListener;

@Log4j
public class SubmarketModule {

    private static final SubmarketChanger[] changers = {
        new MilitaryRegulation(),
        new MilitaryContraband(),
        new ShipDamager(),
    };

    public static void init() {
        boolean hasSubmarket = Global.getSettings().getBoolean("starpocalypseSubmarketModule");
        if (hasSubmarket) {
            log.info("Enabling submarket module");
            new SubmarketListener(changers);
        }
        boolean hasShyBlackMarket = Global.getSettings().getBoolean("starpocalypseSubmarketModuleShyBlackMarket");
        if (hasShyBlackMarket) {
            log.info("Enabling shy black market component");
            new ShyBlackMarketListener();
        }
    }
}
