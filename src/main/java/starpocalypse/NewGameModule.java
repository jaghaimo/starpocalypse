package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import lombok.extern.log4j.Log4j;
import starpocalypse.industry.NewGameListener;
import starpocalypse.submarket.ShipDamager;

@Log4j
public class NewGameModule extends ShipDamager {

    /**
     * Gets called multiple times, as mods could add markets at different stages,
     * e.g. on new game, after economy is loaded, or after time pass.
     */
    public static void init(boolean newGame) {
        boolean hasNewGame = Global.getSettings().getBoolean("starpocalypseNewGameModule");
        if (!hasNewGame) {
            return;
        }
        boolean hasNewGameForce = Global.getSettings().getBoolean("starpocalypseNewGameModuleForce");
        boolean hasNewGamePlayer = Global.getSettings().getBoolean("starpocalypseNewGameModuleDamageStartingFleet");
        if (newGame || hasNewGameForce) {
            log.info("Enabling new game module");
            new NewGameListener();
        }
        if (hasNewGamePlayer && (newGame || hasNewGameForce)) {
            log.info("Damaging player fleet");
            NewGameModule module = new NewGameModule();
            List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
            module.damageShips(members);
        }
    }

    private void damageShips(List<FleetMemberAPI> members) {
        for (FleetMemberAPI member : members) {
            changeShips(null, null, member);
        }
    }
}
