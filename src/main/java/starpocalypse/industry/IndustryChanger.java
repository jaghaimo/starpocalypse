package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class IndustryChanger {

    public void change(MarketAPI market) {
        if (canChange(market)) {
            changeImpl(market);
        }
    }

    protected abstract boolean canChange(MarketAPI market);

    protected abstract void changeImpl(MarketAPI market);

    protected void addMissing(MarketAPI market, String industryId, String... blockingIndustries) {
        if (!hasIndustry(market, blockingIndustries)) {
            log.info("Adding " + industryId);
            market.addIndustry(industryId);
        } else {
            log.info("Skipping not needed " + industryId);
        }
    }

    protected boolean hasIndustry(MarketAPI market, String... blockingIndustries) {
        for (String blocker : blockingIndustries) {
            if (market.hasIndustry(blocker)) {
                return true;
            }
        }
        return false;
    }
}
