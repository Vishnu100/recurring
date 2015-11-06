package com.itranswarp.recurring.subscription.ext.processor.standard.dto;

import java.time.LocalDate;

/**
 * Created by changsure on 15/8/26.
 */
public class StandardSubscriptionCustomData {

    /**
     * TERMED,EVERGREEN
     */
    String termSetting;

    Integer initialTerm;

    Integer renewalTerm;

    /**
     * MONTH,WEEK,DAY
     */
    String termUnit;

    Boolean autoRenew;

    LocalDate termStartDate;

    public String getTermSetting() {
        return termSetting;
    }

    public void setTermSetting(String termSetting) {
        this.termSetting = termSetting;
    }

    public Integer getInitialTerm() {
        return initialTerm;
    }

    public void setInitialTerm(Integer initialTerm) {
        this.initialTerm = initialTerm;
    }

    public Integer getRenewalTerm() {
        return renewalTerm;
    }

    public void setRenewalTerm(Integer renewalTerm) {
        this.renewalTerm = renewalTerm;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public LocalDate getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(LocalDate termStartDate) {
        this.termStartDate = termStartDate;
    }

    public String getTermUnit() {
        return termUnit;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }
}
