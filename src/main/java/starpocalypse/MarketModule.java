package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import lombok.extern.log4j.Log4j;
import starpocalypse.market.IndustryAdder;
import starpocalypse.market.IndustryChanger;
import starpocalypse.market.MarketFixer;
import starpocalypse.market.MarketListener;
import starpocalypse.market.StationAdder;

@Log4j
public class MarketModule {

    private static final boolean hasMarket = Global.getSettings().getBoolean("starpocalypseMarketModule");
    private static final boolean hasMarketStationAdder = Global
        .getSettings()
        .getBoolean("starpocalypseMarketModuleStations");
    private static final IndustryChanger[] changers = {
        new MarketFixer(Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES),
        new MarketFixer(Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND),
        new MarketFixer(
            new String[] { Industries.ORBITALSTATION, Industries.ORBITALSTATION_MID, Industries.ORBITALSTATION_HIGH },
            new String[] {
                Industries.BATTLESTATION,
                Industries.BATTLESTATION_MID,
                Industries.BATTLESTATION_HIGH,
                Industries.STARFORTRESS,
                Industries.STARFORTRESS_MID,
                Industries.STARFORTRESS_HIGH,
            }
        ),
        new IndustryAdder(Industries.GROUNDDEFENSES, true, Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES),
        new IndustryAdder(
            Industries.PATROLHQ,
            false,
            Industries.PATROLHQ,
            Industries.MILITARYBASE,
            Industries.HIGHCOMMAND
        ),
    };

    public static void init() {
        if (hasMarket) {
            log.info("Enabling industry module");
            new MarketListener(changers);
        }
    }

    public static void enableStationAdder() {
        if (hasMarket && hasMarketStationAdder) {
            log.info("Enabling station adder component");
            new MarketListener(new StationAdder());
        }
    }
}
