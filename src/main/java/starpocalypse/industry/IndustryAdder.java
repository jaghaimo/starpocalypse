package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import lombok.extern.log4j.Log4j;

@Log4j
public class IndustryAdder extends IndustryChanger {

    private final String industryId;
    private final boolean allowForHidden;
    private final String[] blockingIndustries;

    public IndustryAdder(String industryId, boolean allowForHidden, String... blockingIndustries) {
        this.industryId = industryId;
        this.allowForHidden = allowForHidden;
        this.blockingIndustries = blockingIndustries;
    }

    @Override
    public void change(MarketAPI market) {
        if (market.isHidden() && !allowForHidden) {
            log.info("Skipping hidden market");
        }
        addMissing(market, industryId, blockingIndustries);
    }
}