package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import lombok.extern.log4j.Log4j;
import starpocalypse.helper.CargoUtils;
import starpocalypse.helper.ConfigHelper;
import starpocalypse.helper.SubmarketUtils;

@Log4j
public class RegulatedOpenMarket extends OpenMarketPlugin {

    private String location;

    @Override
    public void init(SubmarketAPI submarket) {
        super.init(submarket);
        location = SubmarketUtils.getLocation(submarket);
    }

    @Override
    public boolean isIllegalOnSubmarket(String commodityId, TransferAction action) {
        if (!ConfigHelper.wantsRegulation(market.getFactionId())) {
            return super.isIllegalOnSubmarket(commodityId, action);
        }
        if (isAlwaysLegal(commodityId)) {
            return false;
        }
        if (isAlwaysIllegal(commodityId)) {
            return true;
        }
        CommodityOnMarketAPI com = market.getCommodityData(commodityId);
        return com.getCommodity().getTags().contains(Commodities.TAG_MILITARY);
    }

    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {
        if (!ConfigHelper.wantsRegulation(market.getFactionId())) {
            return super.isIllegalOnSubmarket(stack, action);
        }
        String stackName = stack.getDisplayName();
        if (isAlwaysLegal(stackName)) {
            return false;
        }
        if (isAlwaysIllegal(stackName)) {
            return true;
        }
        if (stack.isCommodityStack()) {
            return isIllegalOnSubmarket((String) stack.getData(), action);
        }
        return isSignificant(stack);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
        if (!ConfigHelper.wantsRegulation(market.getFactionId())) {
            return super.isIllegalOnSubmarket(member, action);
        }
        if (isCivilian(member.getVariant())) {
            return false;
        }
        String hullName = getHullName(member);
        if (isAlwaysLegal(hullName)) {
            return false;
        }
        if (isAlwaysIllegal(hullName)) {
            return true;
        }
        return isSignificant(member);
    }

    @Override
    public void updateCargoPrePlayerInteraction() {
        super.updateCargoPrePlayerInteraction();
        if (ConfigHelper.wantsRegulation(market.getFactionId())) {
            removeItems(submarket.getCargo());
            removeShips(submarket.getCargo().getMothballedShips());
        }
    }

    private String getHullName(FleetMemberAPI ship) {
        ShipHullSpecAPI hullSpec = ship.getHullSpec().getBaseHull();
        if (hullSpec == null) {
            hullSpec = ship.getHullSpec();
        }
        return hullSpec.getHullName();
    }

    private boolean isAlwaysIllegal(String name) {
        return ConfigHelper.getRegulationLegal().hasNot(name);
    }

    private boolean isAlwaysLegal(String name) {
        return ConfigHelper.getRegulationLegal().has(name);
    }

    private boolean isCivilian(ShipVariantAPI variant) {
        return variant.hasHullMod(HullMods.CIVGRADE) || variant.getHints().contains(ShipTypeHints.CIVILIAN);
    }

    private boolean isSignificant(CargoStackAPI stack) {
        return CargoUtils.getTier(stack) >= ConfigHelper.getRegulationMinTier();
    }

    private boolean isSignificant(FleetMemberAPI member) {
        return member.getFleetPointCost() >= ConfigHelper.getRegulationMinFP();
    }

    private void removeItems(CargoAPI cargo) {
        for (CargoStackAPI stack : cargo.getStacksCopy()) {
            if (isIllegalOnSubmarket(stack, TransferAction.PLAYER_BUY)) {
                log.info(location + ": Removing " + stack.getDisplayName());
                cargo.removeStack(stack);
            }
        }
        cargo.sort();
    }

    private void removeShips(FleetDataAPI ships) {
        for (FleetMemberAPI member : ships.getMembersListCopy()) {
            if (isIllegalOnSubmarket(member, TransferAction.PLAYER_BUY)) {
                log.info(location + ": Removing " + member.getHullSpec().getHullName());
                ships.removeFleetMember(member);
            }
        }
        ships.sort();
    }
}
