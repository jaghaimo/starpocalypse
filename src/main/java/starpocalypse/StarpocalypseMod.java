package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;

public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        new IndustryChanges();
        new SubmarketChanges();
    }
}
