package starpocalypse.market;

import com.fs.starfarer.api.Global;

/**
 * One off changes to industries during procedural generation.
 */
public class NewGameListener extends MarketListener {

    public NewGameListener() {
        super(new ItemRemover());
    }

    @Override
    public void reportEconomyMonthEnd() {
        Global.getSector().getListenerManager().removeListener(this);
    }
}
