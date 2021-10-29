package starpocalypse.industry;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

public abstract class MarketChanger implements IndustryChanger {

    public void change(MarketAPI market) {
        if (canChange(market)) {
            changeImpl(market);
        }
    }

    protected abstract boolean canChange(MarketAPI market);

    protected abstract void changeImpl(MarketAPI market);
}
