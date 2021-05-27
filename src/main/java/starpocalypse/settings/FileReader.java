package starpocalypse.settings;

import java.io.IOException;

import com.fs.starfarer.api.Global;

import org.json.JSONArray;
import org.json.JSONException;

public class FileReader {

    public JSONArray readCsv(String column, String file) throws JSONException, IOException {
        return Global.getSettings().getMergedSpreadsheetDataForMod(
                column,
                "data/starpocalypse/" + file,
                "starpocalypse"
        );
    }
}
