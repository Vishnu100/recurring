package com.itranswarp.recurring.price;


import java.time.LocalDate;

/**
 * Created by changsure on 15/9/6.
 */
public class PriceInputItem {

    String priceType;

    String priceData;

    LocalDate startDate;

    LocalDate endDate;

    Boolean isFullMonth;

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPriceData() {
        return priceData;
    }

    public void setPriceData(String priceData) {
        this.priceData = priceData;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsFullMonth() {
        return isFullMonth;
    }

    public void setIsFullMonth(Boolean isFullMonth) {
        this.isFullMonth = isFullMonth;
    }

}
