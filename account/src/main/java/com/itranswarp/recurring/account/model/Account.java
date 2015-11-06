package com.itranswarp.recurring.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Account entity.
 *
 * @author michael
 */
@Entity
public class Account extends BaseEntity {

    @Column(length=VARCHAR_100, nullable=false)
    String displayName;

    @Column(length=VARCHAR_1000)
    String description;

    @Column(length = VARCHAR_100)
    String billingType;

    @Column(columnDefinition=COL_TEXT, nullable=false)
    String billingData;

    @Column(length = VARCHAR_100)
    String paymentType;

    @Column(columnDefinition=COL_TEXT, nullable=false)
    String paymentData;

    @Column(length=VARCHAR_100, nullable=false)
    String status;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(String paymentData) {
        this.paymentData = paymentData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class AccountStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String EXPIRED = "EXPIRED";
    }
}
