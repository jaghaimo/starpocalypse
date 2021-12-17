package starpocalypse.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.extern.log4j.Log4j;

@Log4j
/**
 * Changes to market industries are enforced periodically (every economy tick).
 */
public class IndustryListener implements EconomyTickListener {

    protected final IndustryChanger[] changers;

    public IndustryListener(IndustryChanger industryChanger) {
        this(new IndustryChanger[] { industryChanger });
    }

    public IndustryListener(IndustryChanger[] industryChangers) {
        changers = industryChangers;
        Global.getSector().getListenerManager().addListener(this, true);
        reportEconomyTick(0);
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            log.debug("Processing " + market.getName());
            process(market);
        }
    }

    @Override
    public void reportEconomyMonthEnd() {}

    private void process(MarketAPI market) {
        if (Factions.PLAYER.equals(market.getFactionId())) {
            log.debug("Skipping player market");
            return;
        }
        for (IndustryChanger changer : changers) {
            changer.change(market);
        }
    }
}
