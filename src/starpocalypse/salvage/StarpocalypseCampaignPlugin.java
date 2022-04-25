package starpocalypse.salvage;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;

public class StarpocalypseCampaignPlugin extends BaseCampaignPlugin {

    @Override
    public String getId() {
        return "starpocalypseCampaignPlugin";
    }

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public PluginPick<InteractionDialogPlugin> pickInteractionDialogPlugin(SectorEntityToken interactionTarget) {
        if (interactionTarget instanceof CampaignFleetAPI) {
            return new PluginPick<InteractionDialogPlugin>(
                new StarpocalypseFleetInteractionDialogPlugin(),
                PickPriority.MOD_GENERAL
            );
        }
        return super.pickInteractionDialogPlugin(interactionTarget);
    }
}
