package com.itranswarp.recurring.subscription.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

@Entity
public class Subscription extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false)
    String subscriptionDataId;

    @Column(length = ID_LENGTH, nullable = false)
    String accountId;

    @Column(length=VARCHAR_100, nullable=false)
    String status;

    public class SubscriptionStatus {
        public static final String DRAFT = "DRAFT";
        public static final String ACTIVE = "ACTIVE";
        public static final String FINISH = "FINISH";
        public static final String DELETE = "DELETE";
    }

    public String getSubscriptionDataId() {
        return subscriptionDataId;
    }

    public void setSubscriptionDataId(String subscriptionDataId) {
        this.subscriptionDataId = subscriptionDataId;
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
}
