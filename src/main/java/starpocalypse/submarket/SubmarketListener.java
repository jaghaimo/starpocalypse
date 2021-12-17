package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class SubmarketListener implements ColonyInteractionListener {

    private final SubmarketChanger[] changers;

    public SubmarketListener(SubmarketChanger[] submarketChangers) {
        changers = submarketChangers;
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        // This is a workaround for https://fractalsoftworks.com/forum/index.php?topic=23252.0
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
            log.info("Processing submarket " + submarket.getNameOneLine());
            prepare(submarket);
            process(submarket);
        }
    }

    private void prepare(SubmarketAPI submarket) {
        SubmarketPlugin plugin = submarket.getPlugin();
        plugin.updateCargoPrePlayerInteraction();
    }

    private void process(SubmarketAPI submarket) {
        for (SubmarketChanger changer : changers) {
            log.info("Trying " + changer.getClass().getSimpleName());
            changer.change(submarket);
        }
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
                    // Otherwise compare spec ids
                    return submarketA.getSpecId().compareTo(submarketB.getSpecId());
                }
            }
        );
        return submarkets;
    }
}
