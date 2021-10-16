package starpocalypse;

import starpocalypse.industry.IndustryChanger;
import starpocalypse.industry.ItemRemover;

/**
 * One off changes during procedural generation.
 */
public class ProcgenChanges extends IndustryChanges {

    public ProcgenChanges() {
        changers = new IndustryChanger[] { new ItemRemover() };
        reportEconomyTick(0);
    }
}
