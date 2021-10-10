package starpocalypse.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j;

@Log4j
public class SimpleMap extends FileReader {

    @Delegate
    private final Map<String, String> map = new HashMap<>();
    private final String field;

    public SimpleMap(String column, String field, String file) {
        this.field = field;
        load(column, file);
    }

    @Override
    protected void loadData(String column, String file) throws JSONException, IOException {
        JSONArray data = readCsv(column, file);
        log.debug("Reading " + file);
        for (int i = 0; i < data.length(); i++) {
            JSONObject line = data.getJSONObject(i);
            String key = line.getString(column);
            String value = line.getString(field);
            map.put(key, value);
            log.debug("> " + key + " = " + value);
        }
    }
}
