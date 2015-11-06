package com.itranswarp.recurring.product.pricing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class OveragePricingTest extends BaseTest {

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidNegativePricing() {
        getOveragePricing(10, -99.9).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidMaxPricing() {
        getOveragePricing(10, Pricing.MAX_PRICE + 1.0).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidNegativeIncludedUnits() {
        getOveragePricing(-10, 99.9).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidMaxIncludedUnits() {
        getOveragePricing(Pricing.MAX_UNITS + 1.0, 99.9).validate();
    }

    @Test
    public void testZeroOveragePricing() {
        OveragePricing pricing = getOveragePricing(10, 0.0);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(0)), DELTA);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(10.0)), DELTA);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(0, pricing.calculate(new UnitPriceInfo(1000)), DELTA);
    }

    @Test
    public void testNormalOveragePricing() {
        final double UNITS = 10.0;
        final double PRICE = 199.9;
        OveragePricing pricing = getOveragePricing(UNITS, PRICE);
        // in overage:
        assertEquals(0.0, pricing.calculate(new UnitPriceInfo(0)), DELTA);
        assertEquals(0.0, pricing.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(0.0, pricing.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(0.0, pricing.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(0.0, pricing.calculate(new UnitPriceInfo(10.0)), DELTA);
        // out of overage:
        assertEquals(PRICE * 0.1, pricing.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(PRICE * 90, pricing.calculate(new UnitPriceInfo(100.0)), DELTA);
    }

    OveragePricing getOveragePricing(double includedUnits, double price) {
        OveragePricing op = new OveragePricing();
        op.setIncludedUnits(includedUnits);
        op.setPrice(price);
        return op;
    }

}
