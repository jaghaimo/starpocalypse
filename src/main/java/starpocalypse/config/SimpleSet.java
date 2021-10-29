package starpocalypse.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Log4j
public class SimpleSet extends FileReader {

    private final Set<String> set = new HashSet<>();
    private static final String ALL = "all";
    private static final String NOT = "!";

    public SimpleSet(String column, String file) {
        load(column, file);
    }

    public String[] getAll() {
        return set.toArray(new String[0]);
    }

    public boolean has(String value) {
        if (set.contains(value)) {
            return true;
        }
        if (set.contains(NOT + value)) {
            return false;
        }
        if (set.contains(ALL)) {
            return true;
        }
        return false;
    }

    @Override
    protected void loadData(String column, String file) throws JSONException, IOException {
        JSONArray data = readCsv(column, file);
        log.debug("Reading " + file);
        for (int i = 0; i < data.length(); i++) {
            JSONObject line = data.getJSONObject(i);
            String field = line.getString(column);
            set.add(field);
            log.debug("> " + field);
        }
    }
}
