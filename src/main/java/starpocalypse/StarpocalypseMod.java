package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;

public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        ProcgenModule.init();
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        ProcgenModule.init();
    }

    @Override
    public void onNewGameAfterTimePass() {
        ProcgenModule.init();
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        CombatModule.init();
        IndustryModule.init();
        SubmarketModule.init();
    }
}
