package starpocalypse.settings;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j;

@Log4j
public class AllStations extends FileReader {

    private final List<String> allStations = new LinkedList<>();

    public AllStations() {
        try {
            load();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getAll() {
        return allStations.toArray(new String[0]);
    }

    private void load() throws JSONException, IOException {
        JSONArray rawData = new FileReader().readCsv("stations", "allStations.csv");
        log.debug("Reading allStations.csv:");
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject rawLine = rawData.getJSONObject(i);
            String station = rawLine.getString("station");
            allStations.add(station);
            log.debug("> " + station);
        }
    }
}
