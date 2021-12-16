package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class StarpocalypseMod extends BaseModPlugin {

    private final boolean hasEngagement = Global.getSettings().getBoolean("starpocalypseEngagementModule");
    private final boolean hasIndustry = Global.getSettings().getBoolean("starpocalypseIndustryModule");
    private final boolean hasProcGen = Global.getSettings().getBoolean("starpocalypseProcgenModule");
    private final boolean hasProcGenForce = Global.getSettings().getBoolean("starpocalypseProcgenModuleForce");
    private final boolean hasSubmarket = Global.getSettings().getBoolean("starpocalypseSubmarketModule");

    @Override
    public void onNewGameAfterProcGen() {
        ProcgenModule.init(hasProcGen);
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        ProcgenModule.init(hasProcGen);
    }

    @Override
    public void onNewGameAfterTimePass() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        EngagementModule.init(hasEngagement);
        IndustryModule.init(hasIndustry);
        ProcgenModule.init(hasProcGen || hasProcGenForce);
        SubmarketModule.init(hasSubmarket);
    }
}
