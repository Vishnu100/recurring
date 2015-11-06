package com.itranswarp.recurring.product.pricing;

public abstract class BaseTest {

    public static final double DELTA = 0.0000001;

    public static TieredPricing.Tier createFlatFeeTier(double units, double price) {
        TieredPricing.Tier tier = new TieredPricing.Tier();
        tier.units = units;
        tier.price = price;
        tier.priceFormat = TieredPricing.PriceFormat.FlatFee;
        return tier;
    }

    public static TieredPricing.Tier createPerUnitTier(double units, double price) {
        TieredPricing.Tier tier = new TieredPricing.Tier();
        tier.units = units;
        tier.price = price;
        tier.priceFormat = TieredPricing.PriceFormat.PerUnit;
        return tier;
    }
}
