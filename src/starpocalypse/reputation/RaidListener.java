package starpocalypse.reputation;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyPlayerHostileActListener;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD.TempData;

public class RaidListener implements ColonyPlayerHostileActListener {

    @Override
    public void reportRaidForValuablesFinishedBeforeCargoShown(
        InteractionDialogAPI dialog,
        MarketAPI market,
        TempData actionData,
        CargoAPI cargo
    ) {
        CargoAPI raidLoot = actionData.raidLoot;
    }

    @Override
    public void reportRaidToDisruptFinished(
        InteractionDialogAPI dialog,
        MarketAPI market,
        TempData actionData,
        Industry industry
    ) {}

    @Override
    public void reportTacticalBombardmentFinished(InteractionDialogAPI dialog, MarketAPI market, TempData actionData) {}

    @Override
    public void reportSaturationBombardmentFinished(
        InteractionDialogAPI dialog,
        MarketAPI market,
        TempData actionData
    ) {}
}
