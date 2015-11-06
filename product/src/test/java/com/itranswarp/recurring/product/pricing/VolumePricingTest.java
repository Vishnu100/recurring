package com.itranswarp.recurring.product.pricing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class VolumePricingTest extends BaseTest {

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyTier() {
        createVolumePricing().validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWith0() {
        createVolumePricing(
                createFlatFeeTier(0.0, 19.9), // Error: units = 0.0
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(30.0, 37.7)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWithNegative() {
        createVolumePricing(
                createFlatFeeTier(-0.1, 19.9), // Error: units < 0.0
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(30.0, 37.7)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersNotOrdered() {
        createVolumePricing(
                createFlatFeeTier(10.0, 19.9),
                createFlatFeeTier(30.0, 37.7),
                createFlatFeeTier(20.0, 28.8) // Error: units < last
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWithTooLarge() {
        createVolumePricing(
                createFlatFeeTier(10.0, 19.9),
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(Pricing.MAX_UNITS + 1.0, 37.7) // Error: units too large
        ).validate();
    }

    @Test
    public void test3FlatFeeTiers() {
        VolumePricing vp = createVolumePricing(
                createFlatFeeTier(10.0, 19.9),
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(30.0, 37.7)
        );
        // 0 ~ 10.0
        assertEquals(19.9, vp.calculate(new UnitPriceInfo(0)), DELTA);
        assertEquals(19.9, vp.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(19.9, vp.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(19.9, vp.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(19.9, vp.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10.0 ~ 20.0
        assertEquals(28.8, vp.calculate(new UnitPriceInfo(10.01)), DELTA);
        assertEquals(28.8, vp.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(28.8, vp.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(28.8, vp.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20.0 ~ 30.0
        assertEquals(37.7, vp.calculate(new UnitPriceInfo(20.01)), DELTA);
        assertEquals(37.7, vp.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(37.7, vp.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(37.7, vp.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(37.7, vp.calculate(new UnitPriceInfo(30.0)), DELTA);
        // > 30:
        for (double x : new double[] { 30.01, 30.1, 31, 99, 10000 }) {
            assertEquals(37.7, vp.calculate(new UnitPriceInfo(x)), DELTA);
        }
    }

    @Test
    public void test3PerUnitTiers() {
        VolumePricing vp = createVolumePricing(
                createPerUnitTier(10.0, 9.9),
                createPerUnitTier(20.0, 8.8),
                createPerUnitTier(30.0, 7.7)
        );
        // 0 ~ 10.0
        assertEquals(0.0, vp.calculate(new UnitPriceInfo(0)), DELTA);
        assertEquals(0.99, vp.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(9.9, vp.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(9.9 * 9.9, vp.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(99.0, vp.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10.0 ~ 20.0
        assertEquals(8.8 * 10.01, vp.calculate(new UnitPriceInfo(10.01)), DELTA);
        assertEquals(8.8 * 10.1, vp.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(8.8 * 19.9, vp.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(8.8 * 20.0, vp.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20.0 ~ 30.0
        assertEquals(7.7 * 20.01, vp.calculate(new UnitPriceInfo(20.01)), DELTA);
        assertEquals(7.7 * 20.1, vp.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(7.7 * 21.0, vp.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(7.7 * 29.9, vp.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(7.7 * 30.0, vp.calculate(new UnitPriceInfo(30.0)), DELTA);
        // > 30:
        for (double x : new double[] { 30.01, 30.1, 31, 99, 10000 }) {
            assertEquals(7.7 * 30.0, vp.calculate(new UnitPriceInfo(x)), DELTA);
        }
    }

    @Test
    public void test3MixedTiers() {
        VolumePricing vp = createVolumePricing(
                createPerUnitTier(10.0, 9.9),
                createFlatFeeTier(20.0, 129.9),
                createPerUnitTier(30.0, 7.7)
        );
        // 0 ~ 10.0
        assertEquals(0.0, vp.calculate(new UnitPriceInfo(0)), DELTA);
        assertEquals(0.99, vp.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(9.9, vp.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(9.9 * 9.9, vp.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(99.0, vp.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10.0 ~ 20.0
        assertEquals(129.9, vp.calculate(new UnitPriceInfo(10.01)), DELTA);
        assertEquals(129.9, vp.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(129.9, vp.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(129.9, vp.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20.0 ~ 30.0
        assertEquals(7.7 * 20.01, vp.calculate(new UnitPriceInfo(20.01)), DELTA);
        assertEquals(7.7 * 20.1, vp.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(7.7 * 21.0, vp.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(7.7 * 29.9, vp.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(7.7 * 30.0, vp.calculate(new UnitPriceInfo(30.0)), DELTA);
        // > 30:
        for (double x : new double[] { 30.01, 30.1, 31, 99, 10000 }) {
            assertEquals(7.7 * 30.0, vp.calculate(new UnitPriceInfo(x)), DELTA);
        }
    }

    VolumePricing createVolumePricing(TieredPricing.Tier... tiers) {
        VolumePricing vp = new VolumePricing();
        vp.tiers = tiers;
        return vp;
    }

}
