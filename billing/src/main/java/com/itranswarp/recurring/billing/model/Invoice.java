package com.itranswarp.recurring.billing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itranswarp.recurring.db.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Invoice extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false)
    String subscriptionId;

    @Column(length = ID_LENGTH, nullable = false)
    String subscriptionDataId;

    @Column(length = ID_LENGTH, nullable = false)
    String accountId;

    @Column(columnDefinition = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate invoiceDate;

    @Column(length=VARCHAR_100, nullable = false)
    String status;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionDataId() {
        return subscriptionDataId;
    }

    public void setSubscriptionDataId(String subscriptionDataId) {
        this.subscriptionDataId = subscriptionDataId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public class InvoiceStatus {
        public static final String DRAFT = "DRAFT";
        public static final String POSTED = "POSTED";
    }
}

