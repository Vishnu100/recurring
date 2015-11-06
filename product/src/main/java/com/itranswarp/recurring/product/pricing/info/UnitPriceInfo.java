package com.itranswarp.recurring.product.pricing.info;

import com.itranswarp.recurring.product.pricing.Pricing;

public class UnitPriceInfo extends PriceInfo {

    public final double units;

    public UnitPriceInfo(double units) {
        Pricing.validateUnits(units, "units");
        this.units = units;
    }
}
