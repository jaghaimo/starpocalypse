package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

public class IndustryAdder extends MarketChanger {

    private final MarketHelper helper = new MarketHelper();
    private final String industryId;
    private final boolean allowForHidden;
    private final String[] blockingIndustries;

    public IndustryAdder(String industryId, boolean allowForHidden, String... blockingIndustries) {
        this.industryId = industryId;
        this.allowForHidden = allowForHidden;
        this.blockingIndustries = blockingIndustries;
    }

    @Override
    protected boolean canChange(MarketAPI market) {
        if (market.isHidden() && !allowForHidden) {
            return false;
        }
        return true;
    }

    @Override
    protected void changeImpl(MarketAPI market) {
        helper.addMissing(market, industryId, blockingIndustries);
    }
}
