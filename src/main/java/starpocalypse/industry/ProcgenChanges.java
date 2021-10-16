package starpocalypse.industry;

import com.fs.starfarer.api.Global;

/**
 * One off changes to industries during procedural generation.
 */
public class ProcgenChanges extends IndustryChanges {

    public ProcgenChanges() {
        super(new ItemRemover());
    }

    @Override
    public void reportEconomyMonthEnd() {
        Global.getSector().getListenerManager().removeListener(this);
    }
}
