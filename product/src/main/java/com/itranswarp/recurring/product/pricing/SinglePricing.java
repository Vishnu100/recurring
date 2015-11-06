package com.itranswarp.recurring.product.pricing;

public abstract class SinglePricing implements Pricing {

    double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void validate() {
        Pricing.validatePrice(this.price, "price");
    }

}
