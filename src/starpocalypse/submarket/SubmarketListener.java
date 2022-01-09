package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j;

@Log4j
public class SubmarketListener implements ColonyInteractionListener {

    @Delegate
    private final List<SubmarketChanger> changers = new LinkedList<>();

    public void register() {
        if (!changers.isEmpty()) {
            Global.getSector().getListenerManager().addListener(this, true);
        }
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        reportPlayerOpenedMarketAndCargoUpdated(market);
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {}

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
        log.debug("Processing market " + market.getName());
        processSubmarkets(market);
    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {}

    private void processSubmarkets(MarketAPI market) {
        for (SubmarketAPI submarket : getSortedSubmarkets(market)) {
            process(submarket);
        }
    }

    private void process(SubmarketAPI submarket) {
        if (!okToUpdate(submarket)) {
            log.debug("Skipping already updated submarket " + submarket.getNameOneLine());
            return;
        }
        update(submarket);
        log.debug("Processing submarket " + submarket.getNameOneLine());
        for (SubmarketChanger changer : changers) {
            log.debug("Trying " + changer.getClass().getSimpleName());
            changer.change(submarket);
        }
    }

    private boolean okToUpdate(SubmarketAPI submarket) {
        boolean okToUpdate = true;
        try {
            SubmarketPlugin plugin = submarket.getPlugin();
            okToUpdate = ((BaseSubmarketPlugin) plugin).okToUpdateShipsAndWeapons();
        } catch (ClassCastException exception) {
            log.warn("Incompatible plugin for submarket " + submarket.getNameOneLine());
        }
        return okToUpdate;
    }

    private void update(SubmarketAPI submarket) {
        SubmarketPlugin plugin = submarket.getPlugin();
        if (plugin == null) {
            log.warn("Found null plugin for submarket " + submarket.getNameOneLine());
            return;
        }
        plugin.updateCargoPrePlayerInteraction();
    }

    private List<SubmarketAPI> getSortedSubmarkets(MarketAPI market) {
        List<SubmarketAPI> submarkets = market.getSubmarketsCopy();
        Collections.sort(
            submarkets,
            new Comparator<SubmarketAPI>() {
                @Override
                public int compare(SubmarketAPI submarketA, SubmarketAPI submarketB) {
                    // Military Market is to be last
                    if (submarketA.getSpecId().equals(Submarkets.GENERIC_MILITARY)) {
                        return 1;
                    }
                    if (submarketB.getSpecId().equals(Submarkets.GENERIC_MILITARY)) {
                        return -1;
                    }
                    // Otherwise as they are
                    return 0;
                }
            }
        );
        return submarkets;
    }
}
