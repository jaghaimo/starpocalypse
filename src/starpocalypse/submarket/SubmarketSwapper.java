package starpocalypse.submarket;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;
import lombok.extern.log4j.Log4j;
import starpocalypse.helper.ConfigUtils;

@Log4j
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

    private static void replaceSubmarket(MarketAPI market, String oldSubmarketId, String newSubmarketId) {
        SubmarketAPI oldSubmarket = market.getSubmarket(oldSubmarketId);
        if (oldSubmarket == null) {
            log.debug("No old submarket on market " + market.getName());
            return;
        }
        // swap markets
        market.removeSubmarket(oldSubmarketId);
        market.addSubmarket(newSubmarketId);
        // preserve state
        SubmarketAPI newSubmarket = market.getSubmarket(newSubmarketId);
        transferState(oldSubmarket, newSubmarket);
    }

    private static void transferState(SubmarketAPI oldSubmarket, SubmarketAPI newSubmarket) {
        BaseSubmarketPlugin oldPlugin = getBasePlugin(oldSubmarket);
        BaseSubmarketPlugin newPlugin = getBasePlugin(newSubmarket);
        if (oldPlugin == null || newPlugin == null) {
            log.error("Incompatible submarket plugins, submarket state will not be preserved", new Throwable());
            return;
        }
        log.info("Swapping " + oldSubmarket.getSpecId() + " with " + newSubmarket.getSpecId());
        // update values
        newPlugin.setMinSWUpdateInterval(oldPlugin.getMinSWUpdateInterval());
        newPlugin.setSinceLastCargoUpdate(oldPlugin.getSinceLastCargoUpdate());
        newPlugin.setSinceSWUpdate(oldPlugin.getSinceSWUpdate());
        // transfer cargo
        newPlugin.getCargo().clear();
        newPlugin.getCargo().addAll(oldPlugin.getCargo());
        // transfer ships
        newPlugin.getCargo().getMothballedShips().clear();
        for (FleetMemberAPI member : oldPlugin.getCargo().getMothballedShips().getMembersListCopy()) {
            newPlugin.getCargo().getMothballedShips().addFleetMember(member);
        }
    }

    private static BaseSubmarketPlugin getBasePlugin(SubmarketAPI submarket) {
        if (submarket == null) {
            log.error("Null submarket passed", new Throwable());
            return null;
        }
        try {
            return (BaseSubmarketPlugin) (submarket.getPlugin());
        } catch (ClassCastException exception) {
            log.warn("Cannot cast to BaseSubmarketPlugin " + submarket.getSpecId(), exception);
        }
        return null;
    }
}
