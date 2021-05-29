package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class IndustryChanger {

    public abstract void change(MarketAPI market);

    protected void addMissing(MarketAPI market, String industryId, String... blockingIndustries) {
        if (!hasIndustry(market, blockingIndustries)) {
            log.info("> Adding " + industryId);
            market.addIndustry(industryId);
        }
    }

    protected boolean hasIndustry(MarketAPI market, String... blockingIndustries) {
        for (String blocker : blockingIndustries) {
            if (market.hasIndustry(blocker)) {
                log.info("> Skipping already present " + blocker);
                return true;
            }
        }
        return false;
    }
}
