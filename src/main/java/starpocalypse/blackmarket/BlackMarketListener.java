package starpocalypse.blackmarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j;

@Log4j
public class BlackMarketListener implements ColonyInteractionListener {

    private final String SHY_BLACK_MARKET = "shy_black_market";
    private final Map<MarketAPI, SubmarketAPI> marketToSubmarket = new LinkedHashMap<>();

    public BlackMarketListener() {
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        SubmarketAPI blackMarket = market.getSubmarket(Submarkets.SUBMARKET_BLACK);
        if (needsReplace(blackMarket)) {
            log.info("Swapping to fake black market due to transponder being on");
            marketToSubmarket.put(market, blackMarket);
            market.removeSubmarket(Submarkets.SUBMARKET_BLACK);
            market.addSubmarket(SHY_BLACK_MARKET);
        }
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {
        SubmarketAPI blackMarket = marketToSubmarket.get(market);
        if (needsReplace(blackMarket)) {
            log.info("Restoring true black market");
            marketToSubmarket.remove(market);
            market.removeSubmarket(SHY_BLACK_MARKET);
            market.addSubmarket(blackMarket);
        }
    }

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {}

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {}

    private boolean needsReplace(SubmarketAPI blackMarket) {
        return Global.getSector().getPlayerFleet().isTransponderOn() && blackMarket != null;
    }
}
