package com.itranswarp.recurring.price.processor.recurring;

/**
 * Created by changsure on 15/9/6.
 */
public class RecurringData {

    /**
     * YEAR,MONTH,WEEK,DAY
     */
    String priceBase;

    /**
     *
     */
    String prorationType;

    Integer dayOfMonth;

    Integer dayOfYear;

    Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public String getPriceBase() {
        return priceBase;
    }

    public void setPriceBase(String priceBase) {
        this.priceBase = priceBase;
    }

    public String getProrationType() {
        return prorationType;
    }

    public void setProrationType(String prorationType) {
        this.prorationType = prorationType;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Integer dayOfYear) {
        this.dayOfYear = dayOfYear;
    }
}
