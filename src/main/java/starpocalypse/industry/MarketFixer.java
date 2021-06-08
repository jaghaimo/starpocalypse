package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
public class MarketFixer implements IndustryChanger {

    private final MarketHelper helper = new MarketHelper();
    private final String[] industryIds;
    private final String[] blockingIndustries;

    public MarketFixer(String industryId, String... blockingIndustries) {
        this.industryIds = new String[] { industryId };
        this.blockingIndustries = blockingIndustries;
    }

    public MarketFixer(String[] industryIds, String... blockingIndustries) {
        this.industryIds = industryIds;
        this.blockingIndustries = blockingIndustries;
    }

    @Override
    public void change(MarketAPI market) {
        for (String industryId : industryIds) {
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
