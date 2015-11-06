package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;
import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class PerUnitPricing extends SinglePricing {

    @Override
    public double calculate(PriceInfo pi) {
        UnitPriceInfo upi = (UnitPriceInfo) pi;
        return getPrice() * upi.units;
    }

}
