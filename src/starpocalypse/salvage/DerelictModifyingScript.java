package starpocalypse.salvage;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import java.util.List;

public class DerelictModifyingScript implements EveryFrameScript {

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        // Debris fields: ships no longer "hide" in plain sight (debris field)
        for (SectorEntityToken entity : getEntities(Tags.DEBRIS_FIELD)) {
            clearSpecialData(entity);
        }
        // Salvageable derelicts: always require a story point
        for (SectorEntityToken entity : getEntities(Tags.SALVAGEABLE)) {
            clearSpecialData(entity);
        }
    }

    private List<SectorEntityToken> getEntities(String tag) {
        return Global.getSector().getPlayerFleet().getContainingLocation().getEntitiesWithTag(tag);
    }

    private void clearSpecialData(SectorEntityToken entity) {
        MemoryAPI memory = entity.getMemoryWithoutUpdate();
        if (memory.contains(MemFlags.SALVAGE_SPECIAL_DATA)) {
            memory.unset(MemFlags.SALVAGE_SPECIAL_DATA);
        }
    }
}
