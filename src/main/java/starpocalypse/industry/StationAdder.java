package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleMap;
import starpocalypse.config.SimpleSet;

@Log4j
public class StationAdder extends MarketChanger {

    private final MarketHelper helper = new MarketHelper();
    private final SimpleSet stationDatabase = new SimpleSet("station", "stationDatabase.csv");
    private final SimpleMap factionStations = new SimpleMap("faction", "station", "stationFactionMap.csv");

    @Override
    protected boolean canChange(MarketAPI market) {
        if (market.isHidden()) {
            log.debug("Skipping hidden market");
            return false;
        }
        String factionId = market.getFactionId();
        if (!factionStations.containsKey(factionId)) {
            log.warn("No station entry for " + factionId);
            return false;
        }
        return true;
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        String factionId = market.getFactionId();
        helper.addMissing(market, factionStations.get(factionId), stationDatabase.getAll());
    }
}
