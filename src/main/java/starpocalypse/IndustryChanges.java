package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

import lombok.extern.log4j.Log4j;
import starpocalypse.settings.StationDatabase;
import starpocalypse.settings.StationFaction;
import starpocalypse.settings.Whitelist;

@Log4j
public class IndustryChanges implements EconomyTickListener {

    private StationDatabase allStations = new StationDatabase();
    private StationFaction factionStations = new StationFaction();
    private Whitelist whitelist = new Whitelist("industryWhitelist.csv");

    public IndustryChanges() {
        Global.getSector().getListenerManager().addListener(this, true);
        reportEconomyTick(0);
    }

    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            log.info("Processing " + market.getName());
            if (!whitelist.has(market.getFactionId())) {
                log.info("> Skipping non-whitelisted");
                continue;
            }
            addMissing(
                    market,
                    Industries.GROUNDDEFENSES,
                    Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES
            );
            if (market.isHidden()) {
                log.info("> Skipping hidden market");
                continue;
            }
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
            log.info("> Adding " + industryId);
            market.addIndustry(industryId);
        }
    }

    private void addMissingStation(MarketAPI market) {
        String factionId = market.getFactionId();
        if (!factionStations.containsKey(factionId)) {
            log.warn("> No station entry for " + factionId);
            return;
        }
        addMissing(market, factionStations.get(factionId), allStations.getAll());
    }

    private boolean hasIndustry(MarketAPI market, String... blockingIndustries) {
        for (String blocker : blockingIndustries) {
            if (market.hasIndustry(blocker)) {
                log.info("> Skipping already present " + blocker);
                return true;
            }
        }
        return false;
    }
}
