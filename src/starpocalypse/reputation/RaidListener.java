package starpocalypse.reputation;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyPlayerHostileActListener;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD.TempData;
import starpocalypse.config.SimpleSet;

public class RaidListener implements ColonyPlayerHostileActListener {

    private final SimpleSet protectedItems = new SimpleSet("item", "raidProtectorItem.csv");

    public static void register() {
        Global.getSector().getListenerManager().addListener(new RaidListener(), true);
    }

    @Override
    public void reportRaidForValuablesFinishedBeforeCargoShown(
        InteractionDialogAPI dialog,
        MarketAPI market,
        TempData actionData,
        CargoAPI cargo
    ) {
        CargoAPI raidLoot = actionData.raidLoot;
        for (CargoStackAPI stack : raidLoot.getStacksCopy()) {
            String item = getSpecialStackId(stack);
            if (protectedItems.has(item)) {
                setAtWar(market);
                return;
            }
        }
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

    private String getSpecialStackId(CargoStackAPI stack) {
        SpecialItemSpecAPI spec = stack.getSpecialItemSpecIfSpecial();
        if (spec == null) {
            return "";
        }
        return spec.getId();
    }

    private void setAtWar(MarketAPI market) {
        FactionAPI faction = market.getFaction();
        Global.getSector().getPlayerFaction().setRelationship(faction.getId(), -1);
    }
}
