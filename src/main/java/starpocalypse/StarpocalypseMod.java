package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import starpocalypse.blackmarket.BlackMarketListener;

public class StarpocalypseMod extends BaseModPlugin {

    private final boolean hasEngagement = Global.getSettings().getBoolean("starpocalypseEngagementModule");
    private final boolean hasIndustry = Global.getSettings().getBoolean("starpocalypseIndustryModule");
    private final boolean hasPlayer = Global.getSettings().getBoolean("starpocalypsePlayerModule");
    private final boolean hasPlayerForce = Global.getSettings().getBoolean("starpocalypsePlayerModuleForce");
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
        new BlackMarketListener();
        EngagementModule.init(hasEngagement);
        IndustryModule.init(hasIndustry);
        PlayerModule.init((hasPlayer && newGame) || hasPlayerForce);
        ProcgenModule.init((hasProcGen && newGame) || hasProcGenForce);
        SubmarketModule.init(hasSubmarket);
    }
}
