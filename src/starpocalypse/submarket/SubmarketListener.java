package starpocalypse.submarket;

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

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        reportPlayerOpenedMarketAndCargoUpdated(market);
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {}

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
        log.info("Processing market " + market.getName());
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
        BaseSubmarketPlugin plugin = getPlugin(submarket);
        if (plugin == null) {
            return;
        }
        if (!plugin.okToUpdateShipsAndWeapons()) {
            log.debug("Skipping already updated submarket " + submarket.getNameOneLine());
            return;
        }
        plugin.updateCargoPrePlayerInteraction();
        log.info("Processing submarket " + submarket.getNameOneLine());
        for (SubmarketChanger changer : changers) {
            log.debug("Trying " + changer.getClass().getSimpleName());
            changer.change(submarket);
        }
    }

    private BaseSubmarketPlugin getPlugin(SubmarketAPI submarket) {
        SubmarketPlugin plugin = submarket.getPlugin();
        try {
            return (BaseSubmarketPlugin) plugin;
        } catch (ClassCastException exception) {
            log.warn("Skipping incompatible plugin for submarket " + submarket.getNameOneLine());
        }
        return null;
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
