package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.econ.SubmarketAPI;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public abstract class SubmarketChanger {

    public void change(SubmarketAPI submarket) {
        if (canChange(submarket)) {
            changeImpl(submarket);
        }
    }

    protected abstract boolean canChange(SubmarketAPI submarket);

    protected abstract void changeImpl(SubmarketAPI submarket);

    protected boolean acceptFaction(SubmarketAPI submarket, SimpleSet allowedFactions) {
        String factionId = submarket.getMarket().getFactionId();
        if (allowedFactions.has(factionId)) {
            return true;
        }
        log.info("> Skipping unknown faction " + factionId);
        return false;
    }

    protected boolean acceptSubmarket(SubmarketAPI submarket, SimpleSet allowedSubmarkets) {
        String submarketId = submarket.getSpecId();
        if (allowedSubmarkets.has(submarketId)) {
            return true;
        }
        log.info("> Skipping unknown submarket " + submarketId);
        return false;
    }
}
