package starpocalypse;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

import lombok.extern.log4j.Log4j;

@Log4j
public class StarpocalypseMod extends BaseModPlugin {

    @Override
    public void onNewGameAfterProcGen() {
        new ProcgenChanges();
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        onGameLoad(true);
    }

    @Override
    public void onGameLoad(boolean newGame) {
        initEngagementModule();
        initIndustryModule();
        initSubmarketModule();
    }

    private void initEngagementModule() {
        if (Global.getSettings().getBoolean("starpocalypseEngagementModule")) {
            log.info("Enabling engagement module");
            new EngagementListener();
        }
    }

    private void initIndustryModule() {
        if (Global.getSettings().getBoolean("starpocalypseIndustryModule")) {
            log.info("Enabling industry module");
            new IndustryChanges();
        }
    }

    private void initSubmarketModule() {
        if (Global.getSettings().getBoolean("starpocalypseSubmarketModule")) {
            log.info("Enabling submarket module");
            new SubmarketChanges();
        }
    }
}
