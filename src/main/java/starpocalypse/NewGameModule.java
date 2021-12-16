package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import lombok.extern.log4j.Log4j;
import starpocalypse.industry.NewGameListener;
import starpocalypse.submarket.ShipDamager;

@Log4j
public class NewGameModule extends ShipDamager {

    private static final boolean hasNewGame = Global.getSettings().getBoolean("starpocalypseNewGameModule");
    private static final boolean hasNewGameForce = Global.getSettings().getBoolean("starpocalypseNewGameModuleForce");

    /**
     * Gets called multiple times, as mods could add markets at different stages,
     * e.g. on new game, after economy is loaded, or after time pass.
     */
    public static void init(boolean newGame) {
        if (!hasNewGame) {
            return;
        }
        if (newGame || hasNewGameForce) {
            log.info("Enabling new game module");
            new NewGameListener();
        }
    }

    public static void damageShips(boolean newGame) {
        if (!hasNewGame) {
            return;
        }
        boolean hasNewGamePlayer = Global.getSettings().getBoolean("starpocalypseNewGameModuleDamageStartingFleet");
        if (hasNewGamePlayer && (newGame || hasNewGameForce)) {
            log.info("Damaging player fleet");
            (new NewGameModule()).damageShips();
        }
    }

    private void damageShips() {
        List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (FleetMemberAPI member : members) {
            changeShips(null, null, member);
        }
    }
}
