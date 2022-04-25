package starpocalypse.salvage;

import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl;

public class StarpocalypseFleetInteractionDialogPlugin extends FleetInteractionDialogPluginImpl {

    public StarpocalypseFleetInteractionDialogPlugin() {
        this(null);
    }

    public StarpocalypseFleetInteractionDialogPlugin(FIDConfig params) {
        super(params);
        context = new StarpocalypseFleetEncounterContext();
    }
}
