package starpocalypse;

import com.fs.starfarer.api.Global;

import lombok.extern.log4j.Log4j;
import starpocalypse.industry.ProcgenChanges;

@Log4j
public class ProcgenModule {

    /**
     * Gets called multiple times, as mods could add markets at different stages,
     * e.g. on new game, after economy is loaded, or after time pass.
     */
    public static void init() {
        if (Global.getSettings().getBoolean("starpocalypseProcgenModule")) {
            log.info("Enabling procgen module");
            new ProcgenChanges();
        }
    }
}