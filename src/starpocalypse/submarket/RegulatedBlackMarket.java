package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import starpocalypse.helper.ConfigUtils;

public class RegulatedBlackMarket extends BlackMarketPlugin {

    @Override
    public boolean isEnabled(CoreUIAPI ui) {
        if (ConfigUtils.isShyBlackMarket()) {
            return !getTransponderState();
        }
        return true;
    }

    @Override
    public String getTooltipAppendix(CoreUIAPI ui) {
        String tooltip = super.getTooltipAppendix(ui);
        if (ConfigUtils.isShyBlackMarket()) {
            tooltip +=
                "\n\nDue to the heavy military presence, trading on Black Market is only possible with the " +
                "transponder turned off.";
        }
        return tooltip;
    }

    private boolean getTransponderState() {
        return Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
