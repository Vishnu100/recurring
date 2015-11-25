package com.itranswarp.recurring.rating.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itranswarp.recurring.common.util.CustomRawStringDeserialize;
import com.itranswarp.recurring.common.util.CustomRawStringSerialize;
import com.itranswarp.recurring.db.model.BaseEntity;
import org.springframework.cglib.core.Local;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class RatingItem extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String subscriptionDataId;

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String subscriptionChargeSegmentId;

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

    @Column(columnDefinition = "date", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate billingDate;

    @Column
    Boolean isFullMonth;

    @Column
    Double totalPrice;

    public String getSubscriptionDataId() {
        return subscriptionDataId;
    }

    public void setSubscriptionDataId(String subscriptionDataId) {
        this.subscriptionDataId = subscriptionDataId;
    }

    public String getSubscriptionChargeSegmentId() {
        return subscriptionChargeSegmentId;
    }

    public void setSubscriptionChargeSegmentId(String subscriptionChargeSegmentId) {
        this.subscriptionChargeSegmentId = subscriptionChargeSegmentId;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getChargeDataId() {
        return chargeDataId;
    }

    public void setChargeDataId(String chargeDataId) {
        this.chargeDataId = chargeDataId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingItem that = (RatingItem) o;

        if (chargeDataId != null ? !chargeDataId.equals(that.chargeDataId) : that.chargeDataId != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        return !(totalPrice != null ? !totalPrice.equals(that.totalPrice) : that.totalPrice != null);

    }

    @Override
    public int hashCode() {
        int result = chargeDataId != null ? chargeDataId.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        return result;
    }
}

