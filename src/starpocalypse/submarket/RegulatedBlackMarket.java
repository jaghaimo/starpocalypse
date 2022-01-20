package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import starpocalypse.helper.ConfigUtils;

public class RegulatedBlackMarket extends BlackMarketPlugin {

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        if (doesWantShyBlackMarket()) {
            tooltip.addPara(
                "Due to the heavy military presence, trading on Black Market " +
                "is only possible with the transponder turned off.",
                10
            );
        }
        super.createTooltipAfterDescription(tooltip, expanded);
    }

    @Override
    public float getTariff() {
        return ConfigUtils.getBlackMarketFenceCut();
    }

    @Override
    public String getTariffTextOverride() {
        return "Fence Cut";
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
