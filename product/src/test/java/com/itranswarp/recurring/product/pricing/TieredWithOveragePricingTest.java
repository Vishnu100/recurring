package com.itranswarp.recurring.product.pricing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

public class TieredWithOveragePricingTest extends BaseTest {

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyTier() {
        createTieredWithOveragePricing(1.1).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidOveragePriceNegative() {
        createTieredWithOveragePricing(-0.1,
                createFlatFeeTier(10.0, 10.0)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidOveragePriceTooLarge() {
        createTieredWithOveragePricing(Pricing.MAX_PRICE + 1.0,
                createFlatFeeTier(10.0, 10.0)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWith0() {
        createTieredWithOveragePricing(
                9.9,
                createFlatFeeTier(0.0, 19.9), // Error: units = 0.0
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(30.0, 37.7)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWithNegative() {
        createTieredWithOveragePricing(
                9.9,
                createFlatFeeTier(-0.1, 19.9), // Error: units < 0.0
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(30.0, 37.7)
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersNotOrdered() {
        createTieredWithOveragePricing(
                9.9,
                createFlatFeeTier(10.0, 19.9),
                createFlatFeeTier(30.0, 37.7),
                createFlatFeeTier(20.0, 28.8) // Error: units < last
        ).validate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidTiersWithTooLarge() {
        createTieredWithOveragePricing(
                9.9,
                createFlatFeeTier(10.0, 19.9),
                createFlatFeeTier(20.0, 28.8),
                createFlatFeeTier(Pricing.MAX_UNITS + 1.0, 37.7) // Error: units too large
        ).validate();
    }

    @Test
    public void testFlatFeeTiersWithOverage() {
        TieredWithOveragePricing top = createTieredWithOveragePricing(
                3.3,
                createFlatFeeTier(10.0, 9.9),
                createFlatFeeTier(20.0, 8.8),
                createFlatFeeTier(30.0, 7.7));
        // 0 ~ 10:
        assertEquals(0.0, top.calculate(new UnitPriceInfo(0.0)), DELTA);
        assertEquals(9.9, top.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(9.9, top.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(9.9, top.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(9.9, top.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10 ~ 20:
        assertEquals(9.9 + 8.8, top.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(9.9 + 8.8, top.calculate(new UnitPriceInfo(11.0)), DELTA);
        assertEquals(9.9 + 8.8, top.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(9.9 + 8.8, top.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20 ~ 30:
        assertEquals(9.9 + 8.8 + 7.7, top.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7, top.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7, top.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7, top.calculate(new UnitPriceInfo(30.0)), DELTA);
        // overage:
        assertEquals(9.9 + 8.8 + 7.7 + 3.3 * 0.1, top.calculate(new UnitPriceInfo(30.1)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7 + 3.3 * 1.1, top.calculate(new UnitPriceInfo(31.1)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7 + 3.3 * 12.5, top.calculate(new UnitPriceInfo(42.5)), DELTA);
        assertEquals(9.9 + 8.8 + 7.7 + 3.3 * 99.6, top.calculate(new UnitPriceInfo(129.6)), DELTA);
    }

    @Test
    public void testPerUnitTiersWithOverage() {
        TieredWithOveragePricing top = createTieredWithOveragePricing(
                3.3,
                createPerUnitTier(10.0, 9.9),
                createPerUnitTier(20.0, 8.8),
                createPerUnitTier(30.0, 7.7));
        // 0 ~ 10:
        assertEquals(0.0, top.calculate(new UnitPriceInfo(0.0)), DELTA);
        assertEquals(9.9 * 0.1, top.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(9.9 * 1.0, top.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(9.9 * 9.9, top.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(9.9 * 10.0, top.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10 ~ 20:
        assertEquals(9.9 * 10.0 + 8.8 * 0.1, top.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 1.0, top.calculate(new UnitPriceInfo(11.0)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 9.9, top.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0, top.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20 ~ 30:
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 0.1, top.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 1.0, top.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 9.9, top.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 10.0, top.calculate(new UnitPriceInfo(30.0)), DELTA);
        // overage:
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 10.0 + 3.3 * 0.1, top.calculate(new UnitPriceInfo(30.1)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 10.0 + 3.3 * 1.1, top.calculate(new UnitPriceInfo(31.1)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 10.0 + 3.3 * 12.5, top.calculate(new UnitPriceInfo(42.5)), DELTA);
        assertEquals(9.9 * 10.0 + 8.8 * 10.0 + 7.7 * 10.0 + 3.3 * 99.6, top.calculate(new UnitPriceInfo(129.6)), DELTA);
    }

    @Test
    public void testMixedTiersWithOverage() {
        TieredWithOveragePricing top = createTieredWithOveragePricing(
                3.3,
                createPerUnitTier(10.0, 9.9),
                createFlatFeeTier(20.0, 5.5),
                createPerUnitTier(30.0, 7.7));
        // 0 ~ 10:
        assertEquals(0.0, top.calculate(new UnitPriceInfo(0.0)), DELTA);
        assertEquals(9.9 * 0.1, top.calculate(new UnitPriceInfo(0.1)), DELTA);
        assertEquals(9.9 * 1.0, top.calculate(new UnitPriceInfo(1.0)), DELTA);
        assertEquals(9.9 * 9.9, top.calculate(new UnitPriceInfo(9.9)), DELTA);
        assertEquals(9.9 * 10.0, top.calculate(new UnitPriceInfo(10.0)), DELTA);
        // 10 ~ 20:
        assertEquals(9.9 * 10.0 + 5.5, top.calculate(new UnitPriceInfo(10.1)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5, top.calculate(new UnitPriceInfo(11.0)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5, top.calculate(new UnitPriceInfo(19.9)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5, top.calculate(new UnitPriceInfo(20.0)), DELTA);
        // 20 ~ 30:
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 0.1, top.calculate(new UnitPriceInfo(20.1)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 1.0, top.calculate(new UnitPriceInfo(21.0)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 9.9, top.calculate(new UnitPriceInfo(29.9)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 10.0, top.calculate(new UnitPriceInfo(30.0)), DELTA);
        // overage:
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 10.0 + 3.3 * 0.1, top.calculate(new UnitPriceInfo(30.1)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 10.0 + 3.3 * 1.1, top.calculate(new UnitPriceInfo(31.1)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 10.0 + 3.3 * 12.5, top.calculate(new UnitPriceInfo(42.5)), DELTA);
        assertEquals(9.9 * 10.0 + 5.5 + 7.7 * 10.0 + 3.3 * 99.6, top.calculate(new UnitPriceInfo(129.6)), DELTA);
    }

    TieredWithOveragePricing createTieredWithOveragePricing(double overagePrice, TieredPricing.Tier... tiers) {
        TieredWithOveragePricing top = new TieredWithOveragePricing();
        top.setOveragePrice(overagePrice);
        top.tiers = tiers;
        return top;
    }

}
