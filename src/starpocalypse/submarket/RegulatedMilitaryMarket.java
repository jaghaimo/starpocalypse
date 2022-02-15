package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import exerelin.campaign.AllianceManager;
import exerelin.campaign.PlayerFactionStore;
import exerelin.utilities.NexUtilsFaction;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleMap;
import starpocalypse.helper.CargoUtils;
import starpocalypse.helper.ConfigHelper;
import starpocalypse.helper.SubmarketUtils;

@Log4j
public class RegulatedMilitaryMarket extends MilitarySubmarketPlugin {

    private String location;

    @Override
    public void init(SubmarketAPI submarket) {
        super.init(submarket);
        location = SubmarketUtils.getLocation(submarket);
    }

    @Override
    public boolean isIllegalOnSubmarket(String commodityId, TransferAction action) {
        CommodityOnMarketAPI com = market.getCommodityData(commodityId);
        CommoditySpecAPI csa = com.getCommodity();
        if (isStabilityLegal(ConfigHelper.getRegulationStabilityItem(), csa.getBasePrice())) {
            log.debug("Making legal due to low stability " + commodityId);
            return false;
        }
        return super.isIllegalOnSubmarket(commodityId, action);
    }

    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {
        if (isStabilityLegal(ConfigHelper.getRegulationStabilityItem(), stack.getBaseValuePerUnit())) {
            log.debug("Making legal due to low stability " + stack.getDisplayName());
            return false;
        }
        return super.isIllegalOnSubmarket(stack, action);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
        if (isStabilityLegal(ConfigHelper.getRegulationStabilityShip(), member.getBaseValue())) {
            log.debug("Making legal due to low stability " + member.getHullSpec().getHullName());
            return false;
        }
        return super.isIllegalOnSubmarket(member, action);
    }

    @Override
    public void updateCargoPrePlayerInteraction() {
        super.updateCargoPrePlayerInteraction();
        if (ConfigHelper.isRemoveEndgameCargo()) {
            removeItems(submarket.getCargo());
        }
        if (ConfigHelper.isRemoveEndgameShips()) {
            removeShips(submarket.getCargo().getMothballedShips());
        }
    }

    @Override
    protected boolean hasCommission() {
        if (Global.getSettings().getModManager().isModEnabled("nexerelin")) {
            return hasCommissionNex();
        }
        return super.hasCommission();
    }

    private boolean hasCommissionNex() {
        String commissionFaction = NexUtilsFaction.getCommissionFactionId();
        if (hasCommissionNex(commissionFaction)) {
            return true;
        }
        if (hasCommissionNex(PlayerFactionStore.getPlayerFactionId())) {
            return true;
        }
        return submarket.getFaction().getId().equals(commissionFaction);
    }

    private boolean hasCommissionNex(String factionId) {
        if (factionId == null) {
            return false;
        }
        return AllianceManager.areFactionsAllied(factionId, submarket.getFaction().getId());
    }

    private boolean isStabilityLegal(SimpleMap stabilityMap, float baseValue) {
        if (!ConfigHelper.wantsRegulation(market.getFactionId())) {
            return false;
        }
        float stability = submarket.getMarket().getStabilityValue();
        if (stability <= 0) {
            return true;
        }
        if (stability >= 10) {
            return false;
        }
        String stabilityKey = String.format("%.0f", stability);
        if (!stabilityMap.containsKey(stabilityKey)) {
            log.error("Missing stability mapping for key " + stabilityKey);
            return false;
        }
        float stabilityValue = Float.parseFloat(stabilityMap.get(stabilityKey));
        return baseValue < stabilityValue;
    }

    private void removeItems(CargoAPI cargo) {
        for (CargoStackAPI stack : cargo.getStacksCopy()) {
            if (CargoUtils.getTier(stack) >= 3) {
                log.info(location + ": Removing " + stack.getDisplayName());
                cargo.removeStack(stack);
            }
        }
        cargo.sort();
    }

    private void removeShips(FleetDataAPI ships) {
        for (FleetMemberAPI member : ships.getMembersListCopy()) {
            if (member.isCapital()) {
                log.info(location + ": Removing " + member.getHullSpec().getHullName());
                ships.removeFleetMember(member);
            }
        }
        ships.sort();
    }
}
