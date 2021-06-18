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

    private SubmarketAPI militaryMarket;
    private float stability;
    private String stabilityKey;

    @Override
    protected void init(SubmarketAPI submarket) {
        militaryMarket = getSubmarket(submarket, Submarkets.GENERIC_MILITARY);
        stability = submarket.getMarket().getStabilityValue();
        stabilityKey = String.valueOf(stability);
    }

    @Override
    protected boolean canChange(SubmarketAPI submarket) {
        boolean hasMilitaryMarket = militaryMarket != null;
        boolean isBlackMarket = Submarkets.SUBMARKET_BLACK.equals(submarket.getSpecId());
        return hasMilitaryMarket && isBlackMarket && super.canChange(submarket);
    }

    @Override
    protected void changeCargo(SubmarketAPI submarket, CargoAPI cargo, CargoStackAPI stack) {
        if (isInvalid(stack) && isAllowed(submarket, stack)) {
            log.info("Moving contraband to military market " + stack.getDisplayName());
            cargo.addFromStack(stack);
            militaryMarket.getCargo().removeStack(stack);
        }
    }

    @Override
    public void changeShips(SubmarketAPI submarket, FleetDataAPI ships, FleetMemberAPI ship) {
        if (!isInvalid(ship)) {
            return;
        }
        if (isAllowed(submarket, ship)) {
            log.info("Moving contraband to military market " + ship.getHullSpec().getHullName());
            ships.addFleetMember(ship);
            militaryMarket.getCargo().getMothballedShips().removeFleetMember(ship);
        }
    }

    private boolean isAllowed(SubmarketAPI submarket, CargoStackAPI stack) {
        return isAllowed(stabilityShipValues, stack.getBaseValuePerUnit());
    }

    private boolean isAllowed(SubmarketAPI submarket, FleetMemberAPI ship) {
        return isAllowed(stabilityShipValues, ship.getBaseValue());
    }

    private boolean isAllowed(SimpleMap stabilityMap, float value) {
        if (stability <= 0) {
            return false;
        }
        if (stability >= 10) {
            return true;
        }
        float stabilityValue = Float.parseFloat(stabilityCargoValues.get(stabilityKey));
        return value < stabilityValue;
    }
}