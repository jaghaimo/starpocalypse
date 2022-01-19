package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import starpocalypse.submarket.SubmarketSwapper;

/*
 * Traditionally, Starpocalypse has been a utility mod, meaning you can disable the mod at will without breaking
 * your save. Unfortunately, the new implementation requires additional API methods to be added in order to correctly
 * persist progress while still being a utility mod. As such, I have decided to temporarily BREAK the utility
 * aspect of the mod. To enable utility aspect of the mod, set below to true. The consequence of playing with
 * `isUtility` enabled is:
 * 1. Suspicion level will reset each time you save. Trade on black market and get suspicion level to say `extreme`,
 *    leave the market, save the game, dock and notice suspicion level dropping to `none`.
 * 2. Delayed reputation change due to legal or illegal trade will not happen if you save too early after the trade.
 * This option will be removed (and mod becomes a true utility mod again) once new API calls are released.
 * For details see: https://fractalsoftworks.com/forum/index.php?topic=23826
 */
public class UninstallMod extends BaseModPlugin {

    @Override
    public void onGameLoad(boolean newGame) {
        SubmarketSwapper.uninstallLegacy();
        SubmarketSwapper.uninstall();
        SharedData.getData().getPlayerActivityTracker().advance(0);
    }

    @Override
    public void afterGameSave() {
        Global
            .getSector()
            .getCampaignUI()
            .showMessageDialog("You can now safely remove or install new version of Starpocalypse.");
    }
}
