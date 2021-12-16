package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;

public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        ProcgenModule.init(true);
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        ProcgenModule.init(true);
    }

    @Override
    public void onNewGameAfterTimePass() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        EngagementModule.init();
        IndustryModule.init();
        PlayerModule.initPlayer(newGame);
        ProcgenModule.init(newGame);
        SubmarketModule.init();
    }
}
