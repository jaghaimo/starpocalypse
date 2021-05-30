package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
public class MarketFixer extends IndustryChanger {

    private final String industryId;
    private final String[] blockingIndustries;

    public MarketFixer(String industryId, String... blockingIndustries) {
        this.industryId = industryId;
        this.blockingIndustries = blockingIndustries;
    }

    @Override
    protected boolean canChange(MarketAPI market) {
        boolean hasIndustry = hasIndustry(market, industryId);
        boolean hasBlocking = hasIndustry(market, blockingIndustries);
        return hasIndustry && hasBlocking;
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        log.info("Removing duplicate industry " + industryId);
        market.removeIndustry(industryId, null, false);
    }
}
