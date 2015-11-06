package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.product.pricing.info.PriceInfo;

public interface Pricing {

    static final double MAX_PRICE = 100000000.0;

    static final double MAX_USAGE = 10000000.0;

    static final double MAX_UNITS = 1000000.0;

    void validate();

    double calculate(PriceInfo pi);

    static void validatePrice(double x, String name) {
        if (x < 0.0) {
            throw new IllegalArgumentException(name + " is negative: " + x);
        }
        if (x > MAX_PRICE) {
            throw new IllegalArgumentException(name + " is too large: " + x + " (maximum = " + MAX_PRICE + ")");
        }
    }

    static void validateUnits(double x, String name) {
        if (x < 0.0) {
            throw new IllegalArgumentException(name + " is negative: " + x);
        }
        if (x > MAX_UNITS) {
            throw new IllegalArgumentException(name + " is too large: " + x + " (maximum = " + MAX_UNITS + ")");
        }
    }

    static void validateUsage(double x, String name) {
        if (x < 0.0) {
            throw new IllegalArgumentException(name + " is negative: " + x);
        }
        if (x > MAX_USAGE) {
            throw new IllegalArgumentException(name + " is too large: " + x + " (maximum = " + MAX_USAGE + ")");
        }
    }
}
