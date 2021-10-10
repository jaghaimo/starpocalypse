package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import lombok.extern.log4j.Log4j;
import starpocalypse.config.SimpleSet;

@Log4j
public class FakeNanoforge extends MarketChanger {

    private final SimpleSet fakeNanoforge = new SimpleSet("market", "fakeNanoforge.csv");

    @Override
    protected boolean canChange(MarketAPI market) {
        return fakeNanoforge.has(market.getId());
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        log.info("Applying fake nanoforge bonus to " + market.getName());
        market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(
                "nanoforge", ItemEffectsRepo.PRISTINE_NANOFORGE_QUALITY_BONUS, "starpocalypse"
        );
    }
}
