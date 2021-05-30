package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import lombok.extern.log4j.Log4j;
import starpocalypse.industry.IndustryAdder;
import starpocalypse.industry.IndustryChanger;
import starpocalypse.industry.MarketFixer;
import starpocalypse.industry.StationAdder;

@Log4j
public class IndustryChanges implements EconomyTickListener {

    private IndustryChanger[] changers = {
            new MarketFixer(
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            ),
            new MarketFixer(
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            ),
            new MarketFixer(
                    Industries.ORBITALSTATION, Industries.BATTLESTATION, Industries.STARFORTRESS
            ),
            new MarketFixer(
                    Industries.ORBITALSTATION_MID, Industries.BATTLESTATION_MID, Industries.STARFORTRESS_MID
            ),
            new MarketFixer(
                    Industries.ORBITALSTATION_HIGH, Industries.BATTLESTATION_HIGH, Industries.STARFORTRESS_HIGH
            ),
            new IndustryAdder(
                    Industries.GROUNDDEFENSES,
                    true,
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            ),
            new IndustryAdder(
                    Industries.PATROLHQ,
                    true,
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            ),
            new StationAdder()
    };

    public IndustryChanges() {
        Global.getSector().getListenerManager().addListener(this, true);
        reportEconomyTick(0);
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            log.info("Processing " + market.getName());
            process(market);
        }
    }

    @Override
    public void reportEconomyMonthEnd() {
    }

    private void process(MarketAPI market) {
        for (IndustryChanger changer : changers) {
            changer.change(market);
        }
    }
}
