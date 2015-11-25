package com.itranswarp.recurring.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itranswarp.recurring.common.util.CustomRawStringDeserialize;
import com.itranswarp.recurring.common.util.CustomRawStringSerialize;
import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Read-only data for charge data.
 *
 * @author michael
 */
@Entity
public class ChargeData extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false, updatable = false)
    String chargeId;

    @Column(length = VARCHAR_100, nullable = false, updatable = false)
    String name;

    @Column(length = VARCHAR_100, nullable = false, updatable = false)
    String type;

    @Column(length = VARCHAR_1000, nullable = false, updatable = false)
    String description;

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

    @Column(length = VARCHAR_100,nullable = false,updatable = true)
    String status;

    @Column(length = ID_LENGTH, nullable = false, updatable = false)
    String previousId;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousId() {
        return previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
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

    public class ChargeDataStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String EXPIRED = "EXPIRED";
    }

    public class ChargeType{
        public static final String ONE_TIME = "ONE_TIME";
        public static final String RECURRING = "RECURRING";
        public static final String USAGE = "USAGE";
    }
}
