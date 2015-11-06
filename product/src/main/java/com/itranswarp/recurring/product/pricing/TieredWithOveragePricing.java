package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;
import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class TieredWithOveragePricing extends TieredPricing {

    // per unit thereafter
    double overagePrice;

    public double getOveragePrice() {
        return this.overagePrice;
    }

    public void setOveragePrice(double overagePrice) {
        this.overagePrice = overagePrice;
    }

    @Override
    public void validate() {
        super.validate();
        Pricing.validatePrice(this.overagePrice, "overagePrice");
    }

    @Override
    public double calculate(PriceInfo pi) {
        UnitPriceInfo upi = (UnitPriceInfo) pi;
        final double units = upi.units;
        double sum = 0.0;
        double from = 0.0;
        for (Tier tier : this.tiers) {
            if (units <= from) {
                break;
            }
            
            sum += tier.calculate((units > tier.units ? tier.units : units) - from);
            from = tier.units;
        }
        if (units > from) {
            sum += (units - from) * overagePrice;
        }
        return sum;
    }

}
