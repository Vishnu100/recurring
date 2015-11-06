package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;
import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class VolumePricing extends TieredPricing {

    @Override
    public double calculate(PriceInfo pi) {
        UnitPriceInfo upi = (UnitPriceInfo) pi;
        Pricing.validateUnits(upi.units, "volume");
        // find matched tier:
        for (Tier tier : this.tiers) {
            if (upi.units <= tier.units) {
                return tier.calculate(upi.units);
            }
        }
        // since no matched tier, return the last tier with max units:
        Tier last = this.tiers[this.tiers.length - 1];
        return last.calculate(last.units);
    }

}
