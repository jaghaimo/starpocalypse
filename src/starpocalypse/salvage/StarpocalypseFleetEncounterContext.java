package starpocalypse.salvage;

import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.FleetEncounterContext;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StarpocalypseFleetEncounterContext extends FleetEncounterContext {

    private final List<FleetMemberAPI> recoverableShips = new LinkedList<>();
    private final List<FleetMemberAPI> storyRecoverableShips = new LinkedList<>();

    @Override
    public List<FleetMemberAPI> getRecoverableShips(
        BattleAPI battle,
        CampaignFleetAPI winningFleet,
        CampaignFleetAPI otherFleet
    ) {
        recoverableShips.clear();
        recoverableShips.addAll(super.getRecoverableShips(battle, winningFleet, otherFleet));
        storyRecoverableShips.clear();
        storyRecoverableShips.addAll(super.getStoryRecoverableShips());
        return Collections.emptyList();
    }

    @Override
    public List<FleetMemberAPI> getStoryRecoverableShips() {
        List<FleetMemberAPI> allShips = new LinkedList<>();
        allShips.addAll(recoverableShips);
        allShips.addAll(storyRecoverableShips);
        int cutOff = Math.min(23, allShips.size());
        return allShips.subList(0, cutOff);
    }
}
