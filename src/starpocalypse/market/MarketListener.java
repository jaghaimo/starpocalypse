package starpocalypse.market;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import java.util.LinkedList;
import java.util.List;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j;

/**
 * Changes to market industries are enforced periodically (every economy tick).
 */
@Log4j
public class MarketListener implements EconomyTickListener {

    @Delegate
    private final List<MarketChanger> changers = new LinkedList<>();

    public MarketListener() {
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
        for (MarketChanger changer : changers) {
            changer.change(market);
        }
    }
}
