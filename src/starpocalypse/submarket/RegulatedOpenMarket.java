package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Highlights;

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
        return plugin.isIllegalOnSubmarket(commodityId, TransferAction.PLAYER_BUY);
    }

    @Override
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, TransferAction action) {
        return plugin.isIllegalOnSubmarket(stack, TransferAction.PLAYER_BUY);
    }

    @Override
    public boolean isIllegalOnSubmarket(FleetMemberAPI member, TransferAction action) {
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
}