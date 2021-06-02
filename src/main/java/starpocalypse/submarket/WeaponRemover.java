package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class WeaponRemover extends SubmarketChanger {

    private SimpleSet allowedFactions = new SimpleSet("faction", "weaponRemoveFaction.csv");
    private SimpleSet allowedSubmarkets = new SimpleSet("submarket", "weaponRemoveSubmarket.csv");

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean acceptFaction = acceptFaction(submarket, allowedFactions);
        boolean acceptSubmarket = acceptSubmarket(submarket, allowedSubmarkets);
        return acceptFaction && acceptSubmarket;
    }

    @Override
    protected void changeImpl(SubmarketAPI submarket) {
        SubmarketAPI militaryMarket = submarket.getMarket().getSubmarket(Submarkets.GENERIC_MILITARY);
        CargoAPI cargo = submarket.getCargo();
        for (CargoStackAPI stack : cargo.getStacksCopy()) {
            if (!isInvalid(stack)) {
                continue;
            }
            if (militaryMarket != null) {
                log.info("Moving to military " + stack.getDisplayName());
                militaryMarket.getCargo().addFromStack(stack);
            } else {
                log.info("Removing " + stack.getDisplayName());
            }
            cargo.removeStack(stack);
        }
    }

    private boolean isInvalid(CargoStackAPI stack) {
        return stack.isWeaponStack() || stack.isFighterWingStack();
    }
}
