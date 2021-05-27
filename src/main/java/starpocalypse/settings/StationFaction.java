package starpocalypse.settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j;

@Log4j
public class StationFaction extends FileReader {

    @Delegate
    private Map<String, String> factionStations = new HashMap<>();

    public StationFaction() {
        load("stationFaction.csv");
    }

    @Override
    protected void loadData(String file) throws JSONException, IOException {
        JSONArray rawData = readCsv("faction", file);
        log.info("Reading " + file);
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject rawLine = rawData.getJSONObject(i);
            String faction = rawLine.getString("faction");
            String station = rawLine.getString("station");
            put(faction, station);
            log.info("> " + faction + " gets " + station);
        }
    }
}
