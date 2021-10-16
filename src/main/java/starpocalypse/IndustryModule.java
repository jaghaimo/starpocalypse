package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import lombok.extern.log4j.Log4j;
import starpocalypse.industry.IndustryAdder;
import starpocalypse.industry.IndustryChanger;
import starpocalypse.industry.IndustryChanges;
import starpocalypse.industry.MarketFixer;
import starpocalypse.industry.StationAdder;

@Log4j
public class IndustryModule {

    private final static IndustryChanger[] changers = {
            new MarketFixer(
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            ),
            new MarketFixer(
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            ),
            new MarketFixer(
                    new String[] {
                            Industries.ORBITALSTATION, Industries.ORBITALSTATION_MID, Industries.ORBITALSTATION_HIGH
                    },
                    new String[] {
                            Industries.BATTLESTATION, Industries.BATTLESTATION_MID, Industries.BATTLESTATION_HIGH,
                            Industries.STARFORTRESS, Industries.STARFORTRESS_MID, Industries.STARFORTRESS_HIGH
                    }
            ),
            new IndustryAdder(
                    Industries.GROUNDDEFENSES,
                    true,
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            ),
            new IndustryAdder(
                    Industries.PATROLHQ,
                    false,
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            ),
            new StationAdder()
    };

    public static void init() {
        if (Global.getSettings().getBoolean("starpocalypseIndustryModule")) {
            log.info("Enabling industry module");
            new IndustryChanges(changers);
        }
    }
}