package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import starpocalypse.helper.ConfigUtils;
import starpocalypse.market.IndustryAdder;
import starpocalypse.market.MarketListener;
import starpocalypse.market.StationAdder;
import starpocalypse.reputation.EngagementListener;
import starpocalypse.reputation.RaidListener;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.SubmarketSwapper;

@Log4j
public class StarpocalypseMod extends BaseModPlugin {

    private static JSONObject settings;

    @Override
    public void onApplicationLoad() throws Exception {
        settings = Global.getSettings().loadJSON("starpocalypse.json");
        ConfigUtils.init(settings, log);
    }

    @Override
    public void onNewGameAfterTimePass() {
        addDmodsToStartingFleet();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        SubmarketSwapper.uninstallLegacy();
        SubmarketSwapper.reinstall();
        addDmodsToShipsInSubmarkets();
        militaryRegulations();
        industryChanges();
        combatAdjustedReputation();
        hostilityForSpecialItemRaid();
    }

    private void industryChanges() {
        MarketListener listener = new MarketListener();
        addGroundDefenses(listener);
        addPatrolHqs(listener);
        addStations(listener);
        listener.register();
    }

    private void addDmodsToShipsInSubmarkets() {
        if (settings.optBoolean("addDmodsToShipsInSubmarkets", true)) {
            log.info("Enabling ship damager in submarkets");
            ShipDamager.register();
        }
    }

    private void addDmodsToStartingFleet() {
        if (settings.optBoolean("addDmodsToStartingFleet", true)) {
            log.info("Damaging starting fleet");
            List<FleetMemberAPI> members = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
            ShipDamager.apply("player fleet", members);
        }
    }

    private void addGroundDefenses(MarketListener listener) {
        if (settings.optBoolean("addGroundDefenses", true)) {
            log.info("Enabling ground defenses adder");
            listener.add(
                new IndustryAdder(Industries.GROUNDDEFENSES, true, Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES)
            );
        }
    }

    private void addPatrolHqs(MarketListener listener) {
        if (settings.optBoolean("addPatrolHqs", true)) {
            log.info("Enabling patrol hq adder");
            listener.add(
                new IndustryAdder(
                    Industries.PATROLHQ,
                    false,
                    Industries.PATROLHQ,
                    Industries.MILITARYBASE,
                    Industries.HIGHCOMMAND
                )
            );
        }
    }

    private void addStations(MarketListener listener) {
        if (settings.optBoolean("addStations", true)) {
            log.info("Enabling station adder");
            listener.add(new StationAdder());
        }
    }

    private void combatAdjustedReputation() {
        if (settings.optBoolean("combatAdjustedReputation", true)) {
            log.info("Enabling combat adjusted reputation");
            EngagementListener.register();
        }
    }

    private void hostilityForSpecialItemRaid() {
        if (settings.optBoolean("hostilityForSpecialItemRaid", true)) {
            log.info("Enabling hostility for special item raid");
            RaidListener.register();
        }
    }

    private void militaryRegulations() {
        if (settings.optBoolean("militaryRegulations", true)) {
            log.info("Enabling military regulations");
            SubmarketSwapper.register();
        }
    }
}
