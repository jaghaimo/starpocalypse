package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleMap;
import starpocalypse.config.SimpleSet;

@Log4j
public class StationAdder extends IndustryChanger {

    private final SimpleSet stationDatabase = new SimpleSet("station", "stationDatabase.csv");
    private final SimpleMap factionStations = new SimpleMap("faction", "station", "stationFactionMap.csv");

    @Override
    public void change(MarketAPI market) {
        if (market.isHidden()) {
            log.info("Skipping hidden market");
        }
        String factionId = market.getFactionId();
        if (!factionStations.containsKey(factionId)) {
            log.warn("No station entry for " + factionId);
            return;
        }
        addMissing(market, factionStations.get(factionId), stationDatabase.getAll());
    }
}