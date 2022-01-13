package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.SubmarketUpdateListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.List;
import starpocalypse.helper.ConfigUtils;
import starpocalypse.helper.ShipUtils;

public class ShipDamager implements SubmarketUpdateListener {

    public static void apply(List<FleetMemberAPI> members) {
        int minDmods = ConfigUtils.getMinDmods();
        int maxDmods = ConfigUtils.getMaxDmods();
        for (FleetMemberAPI member : members) {
            ShipUtils.damageShip(member, minDmods, maxDmods);
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
        apply(submarket.getCargo().getMothballedShips().getMembersListCopy());
    }

    private static boolean canDamageShips(SubmarketAPI submarket) {
        boolean hasSubmarket = ConfigUtils.getShipDamageSubmarket().has(submarket.getSpecId());
        boolean hasFaction = ConfigUtils.getShipDamageFaction().has(submarket.getMarket().getFactionId());
        return hasSubmarket && hasFaction;
    }
}
