package com.itranswarp.recurring.product.pricing;

import static org.junit.Assert.*;

import org.junit.Test;

public class FlatFeePricingTest extends BaseTest {

    @Test
    public void testCalculateAs0() {
        FlatFeePricing p = new FlatFeePricing();
        p.setPrice(0.0);
        assertEquals(0.0, p.calculate(null), DELTA);
    }

    @Test
    public void testCalculateAsDouble() {
        FlatFeePricing p = new FlatFeePricing();
        p.setPrice(0.1);
        assertEquals(0.1, p.calculate(null), DELTA);
    }

    @Test
    public void testCalculateAsInteger() {
        FlatFeePricing p = new FlatFeePricing();
        p.setPrice(12);
        assertEquals(12.0, p.calculate(null), DELTA);
    }

}
