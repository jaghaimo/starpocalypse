package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        new MarketHardener();
        new SubmarketListener();
    }

    private void setLogLevel(Level level) {
        LogManager.getRootLogger().setLevel(level);
    }
}
