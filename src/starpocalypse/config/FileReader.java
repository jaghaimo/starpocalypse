package starpocalypse.config;

import com.fs.starfarer.api.Global;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;

public abstract class FileReader {

    protected void load(String column, String file) {
        try {
            loadData(column, file);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void loadData(String column, String file) throws JSONException, IOException;

    protected JSONArray readCsv(String column, String file) throws JSONException, IOException {
        return Global.getSettings().getMergedSpreadsheetDataForMod(column, "settings/" + file, "starpocalypse");
    }
}
