package starpocalypse.settings;

import java.io.IOException;

import com.fs.starfarer.api.Global;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class FileReader {

    protected void load(String file) {
        try {
            loadData(file);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void loadData(String file) throws JSONException, IOException;

    protected JSONArray readCsv(String column, String file) throws JSONException, IOException {
        return Global.getSettings().getMergedSpreadsheetDataForMod(
                column,
                "data/starpocalypse/" + file,
                "starpocalypse"
        );
    }
}
