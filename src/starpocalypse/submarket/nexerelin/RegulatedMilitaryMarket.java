package starpocalypse.submarket.nexerelin;

import exerelin.campaign.AllianceManager;
import exerelin.campaign.PlayerFactionStore;
import exerelin.utilities.NexUtilsFaction;

public class RegulatedMilitaryMarket extends starpocalypse.submarket.RegulatedMilitaryMarket {

    @Override
    protected boolean hasCommission() {
        String commissionFaction = NexUtilsFaction.getCommissionFactionId();
        if (
            commissionFaction != null &&
            AllianceManager.areFactionsAllied(commissionFaction, submarket.getFaction().getId())
        ) {
            return true;
        }
        if (
            AllianceManager.areFactionsAllied(PlayerFactionStore.getPlayerFactionId(), submarket.getFaction().getId())
        ) {
            return true;
        }
        return submarket.getFaction().getId().equals(commissionFaction);
    }
}
