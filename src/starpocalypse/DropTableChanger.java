package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemPlugin;
import com.fs.starfarer.api.campaign.SpecialItemSpecAPI;
import com.fs.starfarer.api.campaign.impl.items.MultiBlueprintItemPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import lombok.extern.log4j.Log4j;

@Log4j
public class DropTableChanger {

    public static void removeBlueprintPackages() {
        for (SpecialItemSpecAPI specialItemSpec : Global.getSettings().getAllSpecialItemSpecs()) {
            SpecialItemPlugin plugin = specialItemSpec.getNewPluginInstance(null);
            if (plugin instanceof MultiBlueprintItemPlugin) {
                log.debug("Removing " + specialItemSpec.getName() + " from drop table");
                specialItemSpec.getTags().add(Tags.NO_DROP);
            }
        }
    }
}
