package com.itranswarp.recurring.subscription.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Readonly entity to store each subscription version.
 *
 * @author michael
 */
@Entity
public class SubscriptionData extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false, updatable = false)
    String subscriptionId;

    @Column(length = ID_LENGTH, nullable = false, updatable = false)
    String previousId;

    @Column(length = VARCHAR_100, nullable = true, updatable = false)
    String name;

    @Column(length = VARCHAR_1000, nullable = true, updatable = false)
    String description;

    @Column(length = ID_LENGTH, nullable = false, updatable = false)
    String accountId;

    @Column(updatable = false, nullable = false, columnDefinition = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @Column(updatable = false, nullable = false, columnDefinition = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate;

    @Column(columnDefinition = COL_TEXT, nullable = true, updatable = false)
    String customData;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getPreviousId() {
        return previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }
}
