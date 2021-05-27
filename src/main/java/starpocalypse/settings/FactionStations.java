package starpocalypse.settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j;

@Log4j
public class FactionStations {

    private Map<String, String> factionStations = new HashMap<>();

    public FactionStations() {
        try {
            load();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String faction) {
        return factionStations.get(faction);
    }

    public boolean has(String faction) {
        return factionStations.containsKey(faction);
    }

    private void load() throws JSONException, IOException {
        JSONArray rawData = new FileReader().readCsv("faction", "factionStations.csv");
        log.debug("Reading factionStations.csv:");
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject rawLine = rawData.getJSONObject(i);
            String faction = rawLine.getString("faction");
            String station = rawLine.getString("station");
            factionStations.put(faction, station);
            log.debug("> " + faction + " gets " + station);
        }
    }
}
