package com.itranswarp.recurring.subscription.ext.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Store subscription amend operation.
 * 
 * @author michael
 */
@Entity
public class Amendment extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false)
    String subscriptionId;

    @Column(length=ID_LENGTH, nullable=false)
    String prevSubscriptionDataId;

    @Column(length=ID_LENGTH, nullable=false)
    String nextSubscriptionDataId;

    @Column(length=VARCHAR_100, nullable=false)
    String type;

    @Column(columnDefinition = COL_TEXT, nullable = false, updatable = false)
    String data;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getPrevSubscriptionDataId() {
        return prevSubscriptionDataId;
    }

    public void setPrevSubscriptionDataId(String prevSubscriptionDataId) {
        this.prevSubscriptionDataId = prevSubscriptionDataId;
    }

    public String getNextSubscriptionDataId() {
        return nextSubscriptionDataId;
    }

    public void setNextSubscriptionDataId(String nextSubscriptionDataId) {
        this.nextSubscriptionDataId = nextSubscriptionDataId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
