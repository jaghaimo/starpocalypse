package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import lombok.extern.log4j.Log4j;
import starpocalypse.settings.AllStations;
import starpocalypse.settings.FactionStations;
import starpocalypse.settings.Whitelist;

@Log4j
public class MarketHardenerListener implements EconomyTickListener {

    private AllStations allStations = new AllStations();
    private FactionStations factionStations = new FactionStations();
    private Whitelist whitelist = new Whitelist();

    public MarketHardenerListener() {
        Global.getSector().getListenerManager().addListener(this, true);
        reportEconomyTick(0);
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            log.info("Processing " + market.getName());
            if (!whitelist.has(market.getFactionId())) {
                log.info("> Skipping non-whitelisted market " + market.getName());
                continue;
            }
            if (market.isHidden()) {
                log.info("> Skipping hidden market " + market.getName());
                continue;
            }
            addMissing(
                    market,
                    Industries.GROUNDDEFENSES,
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            );
            addMissing(
                    market,
                    Industries.PATROLHQ,
                    Industries.PATROLHQ, Industries.MILITARYBASE, Industries.HIGHCOMMAND
            );
            addMissingStation(market);
        }
    }

    @Override
    public void reportEconomyMonthEnd() {
    }

    private void addMissing(MarketAPI market, String industryId, String... blockingIndustries) {
        if (!hasIndustry(market, blockingIndustries)) {
            log.info("> Adding " + industryId + " to " + market.getName());
            market.addIndustry(industryId);
        }
    }

    private void addMissingStation(MarketAPI market) {
        String factionId = market.getFactionId();
        if (!factionStations.has(factionId)) {
            log.warn("> No station entry for " + factionId);
            return;
        }
        addMissing(market, factionStations.get(factionId), allStations.getAll());
    }

    private boolean hasIndustry(MarketAPI market, String... blockingIndustries) {
        for (String blocker : blockingIndustries) {
            if (market.hasIndustry(blocker)) {
                log.info("> Market " + market.getName() + " already has " + blocker);
                return true;
            }
        }
        return false;
    }

}
