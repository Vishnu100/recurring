package com.itranswarp.recurring.period.dto;


import java.time.LocalDate;

/**
 * Created by changsure on 15/9/6.
 */
public class PeriodInputItem {

    String billingType;

    String billingData;

    LocalDate startDate;

    LocalDate endDate;

    LocalDate calFromDate;

    LocalDate calToDate;

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBillingData() {
        return billingData;
    }

    public void setBillingData(String billingData) {
        this.billingData = billingData;
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

    public LocalDate getCalFromDate() {
        return calFromDate;
    }

    public void setCalFromDate(LocalDate calFromDate) {
        this.calFromDate = calFromDate;
    }

    public LocalDate getCalToDate() {
        return calToDate;
    }

    public void setCalToDate(LocalDate calToDate) {
        this.calToDate = calToDate;
    }
}
