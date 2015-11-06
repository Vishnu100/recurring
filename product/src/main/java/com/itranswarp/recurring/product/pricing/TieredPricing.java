package com.itranswarp.recurring.product.pricing;

public abstract class TieredPricing implements Pricing {

    public enum PriceFormat {
        FlatFee,
        PerUnit
    }

    public static class Tier {
        double units;
        double price;
        PriceFormat priceFormat;

        double calculate(double actualUnits) {
            double actualPrice = 0.0;
            switch (this.priceFormat) {
            case FlatFee:
                actualPrice = this.price;
                break;
            case PerUnit:
                actualPrice = actualUnits * this.price;
                break;
            default:
                throw new java.lang.AssertionError("Incomplete switch-case for PriceFormat.");
            }
            return actualPrice;
        }
    }

    Tier[] tiers;

    @Override
    public void validate() {
        // check each tiers:
        if (this.tiers.length == 0) {
            throw new IllegalArgumentException("tier is empty.");
        }
        double last = 0.0;
        int n = 0;
        for (Tier tier : this.tiers) {
            n ++;
            Pricing.validatePrice(tier.price, "price of tier #" + n);
            Pricing.validateUnits(tier.units, "units of tier #" + n);
            if (tier.units <= last) {
                throw new IllegalArgumentException("units of tier #" + n + " has invalid units: " + tier.units + ", must be greater than " + last);
            }
            last = tier.units;
        }
    }
}
