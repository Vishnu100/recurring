package com.itranswarp.recurring.subscription.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itranswarp.recurring.common.util.CustomRawStringDeserialize;
import com.itranswarp.recurring.common.util.CustomRawStringSerialize;
import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Store each subscription segment.
 *
 * @author michael
 */
@Entity
public class SubscriptionChargeSegment extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String subscriptionChargeId;

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String chargeDataId;

    @Column(columnDefinition = "date", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @Column(columnDefinition = "date", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate;

    @Column(length = VARCHAR_100, nullable = false, updatable = false)
    String type;

    @Column(length = VARCHAR_100, nullable = false, updatable = false)
    String priceType;

    @Column(columnDefinition = COL_TEXT, nullable = false, updatable = false)
    @JsonDeserialize(using = CustomRawStringDeserialize.class)
    @JsonSerialize(using = CustomRawStringSerialize.class)
    String priceData;

    @Column(length = VARCHAR_100, nullable = false, updatable = false)
    String billingType;

    @Column(columnDefinition = COL_TEXT, nullable = false, updatable = false)
    @JsonDeserialize(using = CustomRawStringDeserialize.class)
    @JsonSerialize(using = CustomRawStringSerialize.class)
    String billingData;

    public class ChargeType {
        public static final String ONE_TIME = "ONE_TIME";
        public static final String RECURRING = "RECURRING";
        public static final String USAGE = "USAGE";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceData() {
        return priceData;
    }

    public void setPriceData(String priceData) {
        this.priceData = priceData;
    }

    public String getBillingData() {
        return billingData;
    }

    public void setBillingData(String billingData) {
        this.billingData = billingData;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getSubscriptionChargeId() {
        return subscriptionChargeId;
    }

    public void setSubscriptionChargeId(String subscriptionChargeId) {
        this.subscriptionChargeId = subscriptionChargeId;
    }

    public String getChargeDataId() {
        return chargeDataId;
    }

    public void setChargeDataId(String chargeDataId) {
        this.chargeDataId = chargeDataId;
    }
}
