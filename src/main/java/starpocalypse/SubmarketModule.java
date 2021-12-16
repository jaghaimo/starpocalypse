package starpocalypse;

import lombok.extern.log4j.Log4j;
import starpocalypse.submarket.MilitaryContraband;
import starpocalypse.submarket.MilitaryRegulation;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.SubmarketChanger;
import starpocalypse.submarket.SubmarketListener;

@Log4j
public class SubmarketModule {

    private static final SubmarketChanger[] changers = {
        new MilitaryRegulation(),
        new MilitaryContraband(),
        new ShipDamager(),
    };

    public static void init(boolean isEnabled) {
        if (isEnabled) {
            log.info("Enabling submarket module");
            new SubmarketListener(changers);
        }
    }
}
