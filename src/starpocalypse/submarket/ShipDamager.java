package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.SubmarketUpdateListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import starpocalypse.helper.ConfigHelper;
import starpocalypse.helper.ShipUtils;
import starpocalypse.helper.SubmarketUtils;

public class ShipDamager implements SubmarketUpdateListener {

    public static void apply(String location, List<FleetMemberAPI> members) {
        int minDmods = ConfigHelper.getMinDmods();
        int maxDmods = ConfigHelper.getMaxDmods();
        for (FleetMemberAPI member : members) {
            ShipUtils.damageShip(location, member, minDmods, maxDmods);
        }
    }

    public static void register() {
        ShipDamager damager = new ShipDamager();
        Global.getSector().getListenerManager().addListener(damager, true);
    }

    @Override
    public void reportSubmarketCargoAndShipsUpdated(SubmarketAPI submarket) {
        if (!canDamageShips(submarket)) {
            return;
        }
        String location = SubmarketUtils.getLocation(submarket);
        apply(location, submarket.getCargo().getMothballedShips().getMembersListCopy());
    }

    private static boolean canDamageShips(SubmarketAPI submarket) {
        boolean hasSubmarket = ConfigHelper.getShipDamageSubmarket().has(submarket.getSpecId());
        boolean hasFaction = ConfigHelper.getShipDamageFaction().has(submarket.getMarket().getFactionId());
        return hasSubmarket && hasFaction;
    }
}
