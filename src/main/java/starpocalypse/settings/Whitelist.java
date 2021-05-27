package starpocalypse.settings;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j;

@Log4j
public class Whitelist extends FileReader {

    private final Set<String> whitelist = new HashSet<>();

    public Whitelist() {
        load();
    }

    public boolean has(String faction) {
        return whitelist.contains(faction);
    }

    @Override
    protected void loadData() throws JSONException, IOException {
        JSONArray rawData = readCsv("faction", "whitelist.csv");
        log.info("Reading whitelist.csv:");
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject rawLine = rawData.getJSONObject(i);
            String station = rawLine.getString("faction");
            whitelist.add(station);
            log.info("> " + station);
        }
    }
}
