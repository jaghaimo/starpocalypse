package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import starpocalypse.helper.ConfigUtils;

public class RegulatedBlackMarket extends BlackMarketPlugin {

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        if (ConfigUtils.isShyBlackMarket()) {
            tooltip.addPara(
                "\n\nDue to the heavy military presence, trading on Black Market " +
                " is only possible with the transponder turned off.",
                10
            );
        }
        super.createTooltipAfterDescription(tooltip, expanded);
    }

    @Override
    public boolean isEnabled(CoreUIAPI ui) {
        if (ConfigUtils.isShyBlackMarket()) {
            return !getTransponderState();
        }
        return true;
    }

    private boolean getTransponderState() {
        return Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
