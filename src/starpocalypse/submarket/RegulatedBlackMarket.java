package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import starpocalypse.helper.ConfigUtils;

public class RegulatedBlackMarket extends BlackMarketPlugin {

    @Override
    public void createTooltip(CoreUIAPI ui, TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltip(ui, tooltip, expanded);
        if (doesWantShyBlackMarket()) {
            tooltip.addPara(
                "Due to the heavy military presence, trading on Black Market " +
                "is only possible with the transponder turned off.",
                10
            );
        }
    }

    @Override
    public float getTariff() {
        return ConfigUtils.getBlackMarketFenceCut() * market.getTariff().getModifiedValue();
    }

    @Override
    public boolean isEnabled(CoreUIAPI ui) {
        if (doesWantShyBlackMarket()) {
            return !getTransponderState();
        }
        return true;
    }

    private boolean doesWantShyBlackMarket() {
        if (!ConfigUtils.isShyBlackMarket()) {
            return false;
        }
        String faction = market.getFactionId();
        return ConfigUtils.getShyBlackMarketFaction().has(faction);
    }

    private boolean getTransponderState() {
        return Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
