package starpocalypse.submarket;

import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleMap;

@Log4j
public class MilitaryContraband extends MilitaryRegulation {

    private final SimpleMap stabilityCargoValues = new SimpleMap("stability", "cargo", "militaryContraband.csv");
    private final SimpleMap stabilityShipValues = new SimpleMap("stability", "ship", "militaryContraband.csv");

    private SubmarketAPI blackMarket;
    private int stability;
    private String stabilityKey;

    @Override
    protected void init(SubmarketAPI submarket) {
        blackMarket = getSubmarket(submarket, Submarkets.SUBMARKET_BLACK);
        stability = (int) submarket.getMarket().getStabilityValue();
        stabilityKey = String.valueOf(stability);
    }

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean hasBlackMarket = blackMarket != null;
        boolean isMilitaryMarket = Submarkets.GENERIC_MILITARY.equals(submarket.getSpecId());
        return hasBlackMarket && isMilitaryMarket;
    }

    @Override
    protected void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack) {
        if (!isInvalid(stack)) {
            return;
        }
        if (isAllowed(submarket, stack)) {
            log.info("Moving contraband to black market " + stack.getDisplayName());
            cargo.removeStack(stack);
            blackMarket.getCargo().addFromStack(stack);
        }
    }

    @Override
    public void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship) {
        if (!isInvalid(ship)) {
            return;
        }
        if (isAllowed(submarket, ship)) {
            log.info("Moving contraband to black market " + ship.getHullSpec().getHullName());
            ships.removeFleetMember(ship);
            blackMarket.getCargo().getMothballedShips().addFleetMember(ship);
        }
    }

    private boolean isAllowed(SubmarketAPI submarket, CargoStackAPI stack) {
        return isAllowed(stabilityCargoValues, stack.getBaseValuePerUnit());
    }

    private boolean isAllowed(SubmarketAPI submarket, FleetMemberAPI ship) {
        return isAllowed(stabilityShipValues, ship.getBaseValue());
    }

    private boolean isAllowed(SimpleMap stabilityMap, float value) {
        if (stability <= 0) {
            return true;
        }
        if (stability >= 10) {
            return false;
        }
        float stabilityValue = Float.parseFloat(stabilityMap.get(stabilityKey));
        return value < stabilityValue;
    }
}
