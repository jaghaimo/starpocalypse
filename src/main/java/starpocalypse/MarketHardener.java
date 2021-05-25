package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;

public class MarketHardener {

    public MarketHardener() {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            if (!canModify(market)) {
                continue;
            }
            addMissing(market, Industries.PATROLHQ);
            addMissing(market, Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES);
            addMissingStation(market);
        }
    }

    private boolean canModify(MarketAPI market) {
        return !market.isHidden();
    }

    private void addMissing(MarketAPI market, String industryId, String... upgrades) {
        boolean hasIndustry = market.hasIndustry(industryId);
        for (String upgrade : upgrades) {
            hasIndustry = hasIndustry || market.hasIndustry(upgrade);
        }
        if (hasIndustry) {
            market.addIndustry(industryId);
        }
    }

    private void addMissingStation(MarketAPI market) {
        String station = "";
        String[] stations = {
                Industries.ORBITALSTATION, Industries.BATTLESTATION, Industries.STARFORTRESS,
                Industries.ORBITALSTATION_MID, Industries.BATTLESTATION_MID, Industries.STARFORTRESS_MID,
                Industries.ORBITALSTATION_HIGH, Industries.BATTLESTATION_HIGH, Industries.STARFORTRESS_HIGH,
        };
        switch (market.getFactionId()) {
        case Factions.HEGEMONY:
        case Factions.LUDDIC_CHURCH:
        case Factions.LUDDIC_PATH:
        case Factions.PIRATES:
            station = Industries.ORBITALSTATION;
            break;
        case Factions.DIKTAT:
        case Factions.INDEPENDENT:
        case Factions.PERSEAN:
            station = Industries.ORBITALSTATION_MID;
            break;
        case Factions.TRITACHYON:
            station = Industries.ORBITALSTATION_HIGH;
            break;
        }
        if (!station.isEmpty()) {
            addMissing(market, station, stations);
        }
    }
}
