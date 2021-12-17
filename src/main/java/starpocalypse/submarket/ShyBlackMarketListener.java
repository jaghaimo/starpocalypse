package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class ShyBlackMarketListener implements ColonyInteractionListener {

    private final String SHY_BLACK_MARKET = "shy_black_market";
    private final SimpleSet allowedFactions = new SimpleSet("faction", "militaryRegulationFaction.csv");
    private final Map<MarketAPI, SubmarketAPI> marketToSubmarket = new LinkedHashMap<>();

    public ShyBlackMarketListener() {
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        SubmarketAPI blackMarket = market.getSubmarket(Submarkets.SUBMARKET_BLACK);
        if (needsReplace(market, blackMarket)) {
            log.info("Swapping to fake black market due to transponder being on");
            marketToSubmarket.put(market, blackMarket);
            market.removeSubmarket(Submarkets.SUBMARKET_BLACK);
            market.addSubmarket(SHY_BLACK_MARKET);
        }
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {
        SubmarketAPI blackMarket = marketToSubmarket.get(market);
        if (needsReplace(market, blackMarket)) {
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

    private boolean needsReplace(MarketAPI market, SubmarketAPI blackMarket) {
        if (!allowedFactions.has(market.getFactionId())) {
            return false;
        }
        if (blackMarket == null) {
            return false;
        }
        return Global.getSector().getPlayerFleet().isTransponderOn();
    }
}
