package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import starpocalypse.submarket.ShipDamager;

public class PlayerModule extends ShipDamager {

    public static void init(boolean isEnabled) {
        if (isEnabled) {
            PlayerModule module = new PlayerModule();
            List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getMembersWithFightersCopy();
            module.damageShips(members);
        }
    }

    private void damageShips(List<FleetMemberAPI> members) {
        for (FleetMemberAPI member : members) {
            changeShips(null, null, member);
        }
    }
}
