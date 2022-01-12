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
        if (market.getSubmarket(Submarkets.SUBMARKET_OPEN) == null) {
            return;
        }
        market.removeSubmarket(Submarkets.SUBMARKET_OPEN);
        market.addSubmarket("regulated_open_market");
    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {}
}
