package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
/**
 * Fixes markets by removing industries if any of the blocking industries exist.
 */
public class MarketFixer implements IndustryChanger {

    private final MarketHelper helper = new MarketHelper();
    private final String[] removedIndustries;
    private final String[] blockingIndustries;

    public MarketFixer(String removedIndustry, String... blockingIndustries) {
        this.removedIndustries = new String[] { removedIndustry };
        this.blockingIndustries = blockingIndustries;
    }

    public MarketFixer(String[] removedIndustries, String[] blockingIndustries) {
        this.removedIndustries = removedIndustries;
        this.blockingIndustries = blockingIndustries;
    }

    @Override
    public void change(MarketAPI market) {
        for (String industryId : removedIndustries) {
            if (canChange(market, industryId)) {
                changeImpl(market, industryId);
            }
        }
    }

    private boolean canChange(MarketAPI market, String industryId) {
        boolean hasIndustry = helper.hasIndustry(market, industryId);
        boolean hasBlocking = helper.hasIndustry(market, blockingIndustries);
        return hasIndustry && hasBlocking;
    }

    private void changeImpl(MarketAPI market, String industryId) {
        log.info("Removing duplicate industry " + industryId);
        market.removeIndustry(industryId, null, false);
    }
}
