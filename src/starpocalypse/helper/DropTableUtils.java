package starpocalypse.helper;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemPlugin;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.campaign.impl.items.MultiBlueprintItemPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import lombok.extern.log4j.Log4j;
import starpocalypse.droplist.DropListModifier;
import starpocalypse.droplist.FighterModifier;
import starpocalypse.droplist.IndustryModifier;
import starpocalypse.droplist.ShipModifier;
import starpocalypse.droplist.WeaponModifier;
import starpocalypse.salvage.DerelictModifyingScript;
import starpocalypse.salvage.StarpocalypseCampaignPlugin;

@Log4j
public class DropTableUtils {

    public static void removeBlueprintPackages() {
        for (SpecialItemSpecAPI specialItemSpec : Global.getSettings().getAllSpecialItemSpecs()) {
            SpecialItemPlugin plugin = specialItemSpec.getNewPluginInstance(null);
            if (plugin instanceof MultiBlueprintItemPlugin) {
                log.debug("Removing " + specialItemSpec.getName() + " from drop table");
                specialItemSpec.getTags().add(Tags.NO_DROP);
                addContentToDropList((MultiBlueprintItemPlugin) plugin);
            }
        }
    }

    public static void makeRecoveryRequireStoryPoint() {
        Global.getSector().registerPlugin(new StarpocalypseCampaignPlugin());
        Global.getSector().addScript(new DerelictModifyingScript());
    }

    private static void addContentToDropList(MultiBlueprintItemPlugin plugin) {
        log.debug("Readding provided content of " + plugin.getName() + " to drop table");
        for (DropListModifier modifier : getAllModifiers(plugin)) {
            modifier.modify();
        }
    }

    private static DropListModifier[] getAllModifiers(MultiBlueprintItemPlugin plugin) {
        DropListModifier[] modifiers = new DropListModifier[4];
        modifiers[0] = new FighterModifier(plugin.getProvidedFighters());
        modifiers[1] = new IndustryModifier(plugin.getProvidedIndustries());
        modifiers[2] = new ShipModifier(plugin.getProvidedShips());
        modifiers[3] = new WeaponModifier(plugin.getProvidedWeapons());
        return modifiers;
    }
}
