package com.itranswarp.recurring.period.processor.multi;

import java.time.LocalDate;

/**
 * Created by changsure on 15/9/6.
 */
public class MultiData {

    /**
     * YEAR,MONTH,WEEK,DAY
     */
    String periodLengthUnit;

    Integer periodLength;

    /**
     * DAY, no use
     * WEEK, 1~7
     * MONTH, 1~31 (31 means End Of Month, 30 means 30 or 29 or 28)
     * YEAR, 1~366 (366 means End of Year)
     */
    Integer billingDay;

    /**
     * Align Date for split proration rating item.
     */
    LocalDate alignDate;

    /**
     * ADVANCE,ARREARS
     */
    String timing;

    public String getPeriodLengthUnit() {
        return periodLengthUnit;
    }

    public void setPeriodLengthUnit(String periodLengthUnit) {
        this.periodLengthUnit = periodLengthUnit;
    }

    public Integer getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(Integer periodLength) {
        this.periodLength = periodLength;
    }

    public Integer getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(Integer billingDay) {
        this.billingDay = billingDay;
    }

    public LocalDate getAlignDate() {
        return alignDate;
    }

    public void setAlignDate(LocalDate alignDate) {
        this.alignDate = alignDate;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
