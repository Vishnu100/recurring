package com.itranswarp.recurring.product.pricing.info;

import com.itranswarp.recurring.product.pricing.Pricing;

public class UsagePriceInfo extends PriceInfo {

    public final double usage;

    public UsagePriceInfo(double usage) {
        Pricing.validateUsage(usage, "usage");
        this.usage = usage;
    }

}
