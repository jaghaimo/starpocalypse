package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;

public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        NewGameModule.init(true);
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        NewGameModule.init(true);
    }

    @Override
    public void onNewGameAfterTimePass() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        IndustryModule.init();
        NewGameModule.init(newGame);
        ReputationModule.init();
        SubmarketModule.init();
    }
}
