package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;
import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class OveragePricing extends SinglePricing {

    double includedUnits;

    public void setIncludedUnits(double includedUnits) {
        this.includedUnits = includedUnits;
    }

    @Override
    public void validate() {
        super.validate();
        Pricing.validateUnits(includedUnits, "includedUnit");
    }

    @Override
    public double calculate(PriceInfo pi) {
        UnitPriceInfo upi = (UnitPriceInfo) pi;
        return upi.units <= includedUnits ? 0.0 : getPrice() * (upi.units - includedUnits);
    }

}
