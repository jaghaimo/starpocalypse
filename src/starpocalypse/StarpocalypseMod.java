package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import org.json.JSONObject;
import starpocalypse.market.IndustryAdder;
import starpocalypse.market.MarketListener;
import starpocalypse.market.StationAdder;
import starpocalypse.reputation.EngagementListener;
import starpocalypse.reputation.RaidListener;
import starpocalypse.submarket.MilitaryContraband;
import starpocalypse.submarket.MilitaryRegulation;
import starpocalypse.submarket.ShipDamager;
import starpocalypse.submarket.SubmarketListener;

public class StarpocalypseMod extends BaseModPlugin {

    private JSONObject settings;

    @Override
    public void onApplicationLoad() throws Exception {
        settings = Global.getSettings().loadJSON("starpocalypse.json");
    }

    @Override
    public void onNewGameAfterTimePass() {
        MarketListener listener = new MarketListener();
        addDmodsToStartingFleet();
        addGroundDefences(listener);
        addPatrolHq(listener);
        addStations(listener);
    }

    private void addDmodsToStartingFleet() {
        if (settings.optBoolean("addDmodsToStartingFleet", true)) {
            StartingFleetDamager.apply(settings);
        }
    }

    private void addGroundDefences(MarketListener listener) {
        if (settings.optBoolean("addGroundDefences", true)) {
            listener.add(
                new IndustryAdder(Industries.GROUNDDEFENSES, true, Industries.GROUNDDEFENSES, Industries.HEAVYBATTERIES)
            );
        }
    }

    private void addPatrolHq(MarketListener listener) {
        if (settings.optBoolean("addPatrolHq", true)) {
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
            listener.add(new StationAdder());
        }
    }

    @Override
    public void onGameLoad(boolean newGame) {
        SubmarketListener listener = new SubmarketListener();
        addDmodsToShipsInSubmarkets(listener);
        militaryRegulations(listener);
        militaryContraband(listener);
        combatAdjustedReputation();
        hostilityForSpecialItemRaid();
    }

    private void hostilityForSpecialItemRaid() {
        if (settings.optBoolean("hostilityForSpecialItemRaid", true)) {
            RaidListener.register();
        }
    }

    private void combatAdjustedReputation() {
        if (settings.optBoolean("combatAdjustedReputation", true)) {
            EngagementListener.register();
        }
    }

    private void addDmodsToShipsInSubmarkets(SubmarketListener listener) {
        if (settings.optBoolean("addDmodsToShipsInSubmarkets", true)) {
            int minDmods = settings.optInt("minimumDmods", 2);
            int maxDmods = settings.optInt("maximumDmods", 4);
            listener.add(new ShipDamager(minDmods, maxDmods));
        }
    }

    private void militaryRegulations(SubmarketListener listener) {
        if (settings.optBoolean("militaryRegulation", true)) {
            listener.add(new MilitaryRegulation());
        }
    }

    private void militaryContraband(SubmarketListener listener) {
        if (settings.optBoolean("militaryContraband", true)) {
            listener.add(new MilitaryContraband());
        }
    }
}
