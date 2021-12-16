package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import lombok.extern.log4j.Log4j;
import starpocalypse.submarket.ShipDamager;

@Log4j
public class PlayerModule extends ShipDamager {

    public static void init(boolean isEnabled) {
        if (isEnabled) {
            log.info("Enabling player module");
            PlayerModule module = new PlayerModule();
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
