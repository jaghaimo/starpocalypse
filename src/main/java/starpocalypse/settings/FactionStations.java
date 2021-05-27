package starpocalypse.settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j;

@Log4j
public class FactionStations extends FileReader {

    private Map<String, String> factionStations = new HashMap<>();

    public FactionStations() {
        load();
    }

    public String get(String faction) {
        return factionStations.get(faction);
    }

    public boolean has(String faction) {
        return factionStations.containsKey(faction);
    }

    @Override
    protected void loadData() throws JSONException, IOException {
        JSONArray rawData = readCsv("faction", "factionStations.csv");
        log.info("Reading factionStations.csv:");
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject rawLine = rawData.getJSONObject(i);
            String faction = rawLine.getString("faction");
            String station = rawLine.getString("station");
            factionStations.put(faction, station);
            log.info("> " + faction + " gets " + station);
        }
    }
}
