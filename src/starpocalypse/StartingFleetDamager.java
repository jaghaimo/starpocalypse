package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import starpocalypse.submarket.ShipDamager;

@Log4j
public class StartingFleetDamager extends ShipDamager {

    private StartingFleetDamager(int minDmods, int maxDmods) {
        super(minDmods, maxDmods);
        List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (FleetMemberAPI member : members) {
            changeShips(null, null, member);
        }
    }

    public static void apply(JSONObject settings) {
        int minDmods = settings.optInt("minimumDmods", 2);
        int maxDmods = settings.optInt("maximumDmods", 4);
        log.info(String.format("Damaging starting fleet with %d to %d d-mods", minDmods, maxDmods));
        new StartingFleetDamager(minDmods, maxDmods);
    }
}
