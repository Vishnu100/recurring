package com.itranswarp.recurring.product.pricing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class PerUnitPricingTest extends BaseTest {

    @Test
    public void testPriceIs0() {
        PerUnitPricing pup = createPerUnitPricing(0.0);
        assertEquals(0.0, pup.calculate(new UnitPriceInfo(0.0)), DELTA);
        assertEquals(0.0, pup.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(0.0, pup.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(0.0, pup.calculate(new UnitPriceInfo(10.0)), DELTA);
    }

    @Test
    public void testPerUnit() {
        PerUnitPricing pup = createPerUnitPricing(1.1);
        assertEquals(0.0, pup.calculate(new UnitPriceInfo(0.0)), DELTA);
        assertEquals(0.11, pup.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(1.1, pup.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(11.0, pup.calculate(new UnitPriceInfo(10.0)), DELTA);
        assertEquals(108.9, pup.calculate(new UnitPriceInfo(99.0)), DELTA);
    }

    PerUnitPricing createPerUnitPricing(double price) {
        PerUnitPricing pup = new PerUnitPricing();
        pup.setPrice(price);
        return pup;
    }

}
