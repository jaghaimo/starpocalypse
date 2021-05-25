package starpocalypse;

import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.SubmarketInteractionListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

public class SubmarketListener implements SubmarketInteractionListener {

    public SubmarketListener() {
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportPlayerOpenedSubmarket(SubmarketAPI submarket, SubmarketInteractionType type) {
        // leave military and custom markets as-is
        if (!canModify(submarket)) {
            return;
        }
        clearCargo(submarket);
        clearShips(submarket);

    }

    private boolean canModify(SubmarketAPI submarket) {
        SubmarketPlugin plugin = submarket.getPlugin();
        return plugin.isOpenMarket() || plugin.isBlackMarket();
    }

    private void clearCargo(SubmarketAPI submarket) {
        CargoAPI cargo = submarket.getCargo();
        for (CargoStackAPI stack : cargo.getStacksCopy()) {
            if (isInvalid(stack)) {
                cargo.removeStack(stack);
            }
        }
    }

    private void clearShips(SubmarketAPI submarket) {
        CargoAPI cargo = submarket.getCargo();
        FleetDataAPI ships = cargo.getMothballedShips();
        for (FleetMemberAPI ship : ships.getMembersListCopy()) {
            removeOrBreak(ships, ship);
        }
    }

    private void removeOrBreak(FleetDataAPI ships, FleetMemberAPI ship) {
        if (isInvalid(ship)) {
            ships.removeFleetMember(ship);
            return;
        }
        if (!ship.getVariant().isDHull()) {
            DModManager.addDMods(ship, false, 2, new Random());
        }
    }

    private boolean isInvalid(CargoStackAPI stack) {
        return stack.isWeaponStack() || stack.isFighterWingStack();
    }

    private boolean isInvalid(FleetMemberAPI fleetMember) {
        return !fleetMember.getVariant().hasHullMod(HullMods.CIVGRADE);
    }
}
