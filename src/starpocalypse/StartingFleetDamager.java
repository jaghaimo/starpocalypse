package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import lombok.extern.log4j.Log4j;
import starpocalypse.helper.ConfigUtils;
import starpocalypse.helper.ShipUtils;

@Log4j
public class StartingFleetDamager {

    public static void apply() {
        int minDmods = ConfigUtils.getMinDmods();
        int maxDmods = ConfigUtils.getMaxDmods();
        log.info(String.format("Damaging starting fleet with %d to %d d-mods", minDmods, maxDmods));
        List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (FleetMemberAPI member : members) {
            ShipUtils.damageShip(member, minDmods, maxDmods);
        }
    }
}
