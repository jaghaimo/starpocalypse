package starpocalypse;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;

import lombok.extern.log4j.Log4j;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.ShipRemover;
import starpocalypse.submarket.SubmarketChanger;
import starpocalypse.submarket.WeaponRemover;

@Log4j
public class SubmarketChanges implements ColonyInteractionListener {

    private final SubmarketChanger[] changers = {
            new WeaponRemover(),
            new ShipRemover(),
            new ShipDamager()
    };

    public SubmarketChanges() {
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        log.info("Processing market " + market.getName());
        processSubmarkets(market);
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {
    }

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {
    }

    private void processSubmarkets(MarketAPI market) {
        for (SubmarketAPI submarket : market.getSubmarketsCopy()) {
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
            changer.change(submarket);
        }
    }
}
