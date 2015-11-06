package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;

public class FlatFeePricing extends SinglePricing {

    @Override
    public double calculate(PriceInfo pi) {
        return getPrice();
    }

}
