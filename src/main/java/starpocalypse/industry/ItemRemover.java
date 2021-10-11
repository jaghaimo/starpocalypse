package starpocalypse.industry;

import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class ItemRemover extends MarketChanger {

    private final SimpleSet faction = new SimpleSet("faction", "itemRemover.csv");

    @Override
    protected boolean canChange(MarketAPI market) {
        return faction.has(market.getFactionId());
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        String marketName = market.getName();
        for (Industry industry : market.getIndustries()) {
            removeSpecialItemIfPreset(industry, marketName);
        }
    }

    private void removeSpecialItemIfPreset(Industry industry, String marketName) {
        SpecialItemData item = industry.getSpecialItem();
        if (item == null) {
            return;
        }
        log.info("Removing " + item.getId() + " from " + marketName);
    }
}
