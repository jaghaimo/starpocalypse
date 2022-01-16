package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import starpocalypse.helper.ConfigUtils;

public class SubmarketSwapper implements ColonyInteractionListener {

    public static void register() {
        SubmarketSwapper swapper = new SubmarketSwapper();
        Global.getSector().getListenerManager().addListener(swapper, true);
    }

    public static void uninstall() {
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            replaceSubmarket(market, "regulated_open_market", Submarkets.SUBMARKET_OPEN);
            replaceSubmarket(market, "regulated_generic_military", Submarkets.GENERIC_MILITARY);
            replaceSubmarket(market, "regulated_black_market", Submarkets.SUBMARKET_BLACK);
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
        if (!ConfigUtils.getRegulatedFaction().has(market.getFactionId())) {
            return;
        }
        replaceSubmarket(market, Submarkets.SUBMARKET_OPEN, "regulated_open_market");
        replaceSubmarket(market, Submarkets.GENERIC_MILITARY, "regulated_generic_military");
        replaceSubmarket(market, Submarkets.SUBMARKET_BLACK, "regulated_black_market");
    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {}

    private static void replaceSubmarket(MarketAPI market, String oldSubmarket, String newSubmarket) {
        if (market.getSubmarket(oldSubmarket) == null) {
            return;
        }
        market.removeSubmarket(oldSubmarket);
        market.addSubmarket(newSubmarket);
    }
}
