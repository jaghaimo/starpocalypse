package starpocalypse.helper;

import com.fs.starfarer.api.campaign.SubmarketPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ListenerUtil;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;
import lombok.extern.log4j.Log4j;

@Log4j
public class SubmarketUtils {

    public static String getLocation(SubmarketAPI submarket) {
        return String.format("%s/%s", submarket.getMarket().getName(), submarket.getNameOneLine());
    }

    public static void replaceSubmarkets(MarketAPI market, boolean forceReplace) {
        replaceSubmarket(market, Submarkets.SUBMARKET_OPEN, forceReplace);
        replaceSubmarket(market, Submarkets.GENERIC_MILITARY, forceReplace);
        replaceSubmarket(market, Submarkets.SUBMARKET_BLACK, forceReplace);
    }

    public static void replaceSubmarket(MarketAPI market, String submarketId, boolean forceReplace) {
        replaceSubmarket(market, submarketId, submarketId, forceReplace);
    }

    public static void replaceSubmarket(
        MarketAPI market,
        String oldSubmarketId,
        String newSubmarketId,
        boolean forceReplace
    ) {
        SubmarketAPI oldSubmarket = market.getSubmarket(oldSubmarketId);
        if (oldSubmarket == null) {
            log.debug("No old submarket on market " + market.getName());
            return;
        }
        if (isRegulated(oldSubmarket) && !forceReplace) {
            log.debug("Skipping already regulated submarket " + oldSubmarket.getNameOneLine());
            return;
        }
        market.removeSubmarket(oldSubmarketId);
        market.addSubmarket(newSubmarketId);
        SubmarketAPI newSubmarket = market.getSubmarket(newSubmarketId);
        transferCargo(oldSubmarket, newSubmarket);
        transferState(oldSubmarket, newSubmarket);
    }

    public static void updateSubmarkets(MarketAPI market) {
        for (SubmarketAPI submarket : market.getSubmarketsCopy()) {
            updateSubmarket(submarket);
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

    private static boolean isRegulated(SubmarketAPI submarket) {
        String simpleName = submarket.getPlugin().getClass().getSimpleName();
        return simpleName.startsWith("Regulated");
    }

    private static void transferCargo(SubmarketAPI oldSubmarket, SubmarketAPI newSubmarket) {
        SubmarketPlugin oldPlugin = oldSubmarket.getPlugin();
        SubmarketPlugin newPlugin = newSubmarket.getPlugin();
        log.debug("Transferring cargo and ships from " + oldSubmarket.getSpecId() + " to " + newSubmarket.getSpecId());
        newPlugin.getCargo().clear();
        newPlugin.getCargo().addAll(oldPlugin.getCargo());
        newPlugin.getCargo().getMothballedShips().clear();
        for (FleetMemberAPI member : oldPlugin.getCargo().getMothballedShips().getMembersListCopy()) {
            newPlugin.getCargo().getMothballedShips().addFleetMember(member);
        }
    }

    private static void transferState(SubmarketAPI oldSubmarket, SubmarketAPI newSubmarket) {
        BaseSubmarketPlugin oldPlugin = getBasePlugin(oldSubmarket);
        BaseSubmarketPlugin newPlugin = getBasePlugin(newSubmarket);
        if (oldPlugin == null || newPlugin == null) {
            log.error("Incompatible submarket plugins, submarket state will not be preserved");
            return;
        }
        log.debug("Transferring state from " + oldSubmarket.getSpecId() + " to " + newSubmarket.getSpecId());
        newPlugin.setMinSWUpdateInterval(oldPlugin.getMinSWUpdateInterval());
        newPlugin.setSinceLastCargoUpdate(oldPlugin.getSinceLastCargoUpdate());
        newPlugin.setSinceSWUpdate(oldPlugin.getSinceSWUpdate());
    }

    private static void updateSubmarket(SubmarketAPI submarket) {
        submarket.getPlugin().updateCargoPrePlayerInteraction();
        ListenerUtil.reportSubmarketCargoAndShipsUpdated(submarket);
    }
}
