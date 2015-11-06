package com.itranswarp.recurring.period.dto;


import java.time.LocalDate;

/**
 * Created by changsure on 15/9/6.
 */
public class PeriodOutputItem {

    LocalDate startDate;

    LocalDate endDate;

    LocalDate billingDate;

    Boolean isFullMonth;

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

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public Boolean getIsFullMonth() {
        return isFullMonth;
    }

    public void setIsFullMonth(Boolean isFullMonth) {
        this.isFullMonth = isFullMonth;
    }
}
