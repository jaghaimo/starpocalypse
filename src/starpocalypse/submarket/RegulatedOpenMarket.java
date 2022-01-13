package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShipTypeHints;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Highlights;
import starpocalypse.config.SimpleMap;
import starpocalypse.helper.ConfigUtils;

public class RegulatedOpenMarket extends OpenMarketPlugin {

    private MilitarySubmarketPlugin plugin;

    @Override
    public void init(SubmarketAPI submarket) {
        super.init(submarket);
        plugin = new MilitarySubmarketPlugin();
        plugin.init(submarket);
    }

    @Override
    public boolean isIllegalOnSubmarket(String commodityId, TransferAction action) {
        if (isAlwaysLegal(commodityId)) {
            return false;
        }
        return plugin.isIllegalOnSubmarket(commodityId, TransferAction.PLAYER_BUY);
    }

    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {
        if (isAlwaysLegal(stack.getDisplayName())) {
            return false;
        }
        if (isStabilityLegal(ConfigUtils.getRegulatedStabilityItem(), stack.getBaseValuePerUnit())) {
            return false;
        }
        return plugin.isIllegalOnSubmarket(stack, TransferAction.PLAYER_BUY);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
        if (isCivilian(member.getVariant())) {
            return false;
        }
        if (isAlwaysLegal(member)) {
            return false;
        }
        if (isStabilityLegal(ConfigUtils.getRegulatedStabilityShip(), member.getBaseValue())) {
            return false;
        }
        return plugin.isIllegalOnSubmarket(member, TransferAction.PLAYER_BUY);
    }

    @Override
    public String getIllegalTransferText(CargoStackAPI stack, TransferAction action) {
        return plugin.getIllegalTransferText(stack, TransferAction.PLAYER_BUY);
    }

    @Override
    public String getIllegalTransferText(FleetMemberAPI member, TransferAction action) {
        return plugin.getIllegalTransferText(member, TransferAction.PLAYER_BUY);
    }

    @Override
    public Highlights getIllegalTransferTextHighlights(CargoStackAPI stack, TransferAction action) {
        return plugin.getIllegalTransferTextHighlights(stack, TransferAction.PLAYER_BUY);
    }

    @Override
    public Highlights getIllegalTransferTextHighlights(FleetMemberAPI member, TransferAction action) {
        return plugin.getIllegalTransferTextHighlights(member, TransferAction.PLAYER_BUY);
    }

    @Override
    public void updateCargoPrePlayerInteraction() {
        boolean okToUpdate = okToUpdateShipsAndWeapons();
        super.updateCargoPrePlayerInteraction();
        if (okToUpdate && ConfigUtils.isLegacyMilitaryRegulations()) {
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

    private boolean isStabilityLegal(SimpleMap stabilityMap, float baseValue) {
        float stability = submarket.getMarket().getStabilityValue();
        String stabilityKey = String.valueOf(stability);
        if (stability <= 0) {
            return true;
        }
        if (stability >= 10) {
            return false;
        }
        float stabilityValue = Float.parseFloat(stabilityMap.get(stabilityKey));
        return baseValue < stabilityValue;
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
