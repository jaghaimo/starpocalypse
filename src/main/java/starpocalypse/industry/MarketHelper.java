package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import lombok.extern.log4j.Log4j;

@Log4j
public class MarketHelper {

    public void addMissing(MarketAPI market, String industryId, String... blockingIndustries) {
        if (!hasIndustry(market, blockingIndustries)) {
            log.info("Adding industry " + industryId);
            market.addIndustry(industryId);
        }
    }

    public boolean hasIndustry(MarketAPI market, String... blockingIndustries) {
        for (String blocker : blockingIndustries) {
            if (market.hasIndustry(blocker)) {
                return true;
            }
        }
        return false;
    }
}
