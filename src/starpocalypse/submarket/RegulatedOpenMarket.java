package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import starpocalypse.helper.ConfigUtils;

public class RegulatedOpenMarket extends OpenMarketPlugin {

    @Override
    public boolean isIllegalOnSubmarket(String commodityId, TransferAction action) {
        if (isAlwaysLegal(commodityId)) {
            return false;
        }
        CommodityOnMarketAPI com = market.getCommodityData(commodityId);
        return com.getCommodity().getTags().contains(Commodities.TAG_MILITARY);
    }

    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {
        if (isAlwaysLegal(stack.getDisplayName())) {
            return false;
        }
        if (stack.isCommodityStack()) {
            return isIllegalOnSubmarket((String) stack.getData(), action);
        }
        return isSignificant(stack);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
        if (isCivilian(member.getVariant())) {
            return false;
        }
        if (isAlwaysLegal(member)) {
            return false;
        }
        return isSignificant(member);
    }

    @Override
    public void updateCargoPrePlayerInteraction() {
        boolean okToUpdate = okToUpdateShipsAndWeapons();
        super.updateCargoPrePlayerInteraction();
        if (okToUpdate) {
            removeItems(submarket.getCargo());
            removeShips(submarket.getCargo().getMothballedShips());
        }
    }

    private boolean isAlwaysLegal(String name) {
        return ConfigUtils.getRegulatedLegal().has(name);
    }

    protected boolean isAlwaysLegal(FleetMemberAPI ship) {
        ShipHullSpecAPI hullSpec = ship.getHullSpec().getBaseHull();
        if (hullSpec == null) {
            hullSpec = ship.getHullSpec();
        }
        return isAlwaysLegal(hullSpec.getHullName());
    }

    private boolean isCivilian(ShipVariantAPI variant) {
        return variant.hasHullMod(HullMods.CIVGRADE) || variant.getHints().contains(ShipTypeHints.CIVILIAN);
    }

    private boolean isSignificant(CargoStackAPI stack) {
        int tier = 0;
        if (stack.isWeaponStack()) {
            WeaponSpecAPI spec = stack.getWeaponSpecIfWeapon();
            tier = spec.getTier();
        } else if (stack.isModSpecStack()) {
            HullModSpecAPI spec = stack.getHullModSpecIfHullMod();
            tier = spec.getTier();
        } else if (stack.isFighterWingStack()) {
            FighterWingSpecAPI spec = stack.getFighterWingSpecIfWing();
            tier = spec.getTier();
        }
        return tier > 0;
    }

    private boolean isSignificant(FleetMemberAPI member) {
        return member.getFleetPointCost() > 5;
    }

    private void removeItems(CargoAPI cargo) {
        for (CargoStackAPI stack : cargo.getStacksCopy()) {
            if (isIllegalOnSubmarket(stack, TransferAction.PLAYER_BUY)) {
                cargo.removeStack(stack);
            }
        }
        cargo.sort();
    }

    private void removeShips(FleetDataAPI ships) {
        for (FleetMemberAPI member : ships.getMembersListCopy()) {
            if (isIllegalOnSubmarket(member, TransferAction.PLAYER_BUY)) {
                ships.removeFleetMember(member);
            }
        }
        ships.sort();
    }
}
