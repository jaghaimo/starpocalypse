package starpocalypse.blackmarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;

public class ShyBlackMarketPlugin extends BlackMarketPlugin {

    @Override
    public boolean isEnabled(CoreUIAPI ui) {
        return !Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
