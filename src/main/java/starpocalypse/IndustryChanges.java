package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import lombok.extern.log4j.Log4j;
import starpocalypse.industry.IndustryAdder;
import starpocalypse.industry.IndustryChanger;
import starpocalypse.industry.MarketFixer;
import starpocalypse.industry.ItemRemover;
import starpocalypse.industry.StationAdder;

@Log4j
public class IndustryChanges implements EconomyTickListener {

    protected IndustryChanger[] changers = {
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
                    true,
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            ),
            new ItemRemover(),
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
        if (Factions.PLAYER.equals(market.getFactionId())) {
            log.info("Skipping player market");
            return;
        }
        for (IndustryChanger changer : changers) {
            changer.change(market);
        }
    }
}
