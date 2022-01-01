package starpocalypse.market;

import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.InstallableItemEffect;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class ItemRemover extends MarketChanger {

    private final SimpleSet factions = new SimpleSet("faction", "itemRemoverFaction.csv");
    private final SimpleSet items = new SimpleSet("item", "itemRemoverItem.csv");

    @Override
    protected boolean canChange(MarketAPI market) {
        return factions.has(market.getFactionId());
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        for (Industry industry : market.getIndustries()) {
            removeSpecialItemIfPresent(industry);
        }
    }

    private void removeSpecialItemIfPresent(Industry industry) {
        SpecialItemData item = industry.getSpecialItem();
        if (!canRemove(item)) {
            return;
        }
        log.info("Removing item " + item.getId());
        InstallableItemEffect itemEffect = ItemEffectsRepo.ITEM_EFFECTS.get(item.getId());
        if (itemEffect != null) {
            itemEffect.unapply(industry);
        }
        industry.setSpecialItem(null);
    }

    private boolean canRemove(SpecialItemData item) {
        if (item == null) {
            return false;
        }
        return items.has(item.getId());
    }
}
