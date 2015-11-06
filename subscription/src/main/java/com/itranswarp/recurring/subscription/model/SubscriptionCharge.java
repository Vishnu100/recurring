package com.itranswarp.recurring.subscription.model;

import com.itranswarp.recurring.db.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Store each subscription segment.
 * 
 * @author michael
 */
@Entity
public class SubscriptionCharge extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String chargeDataId;

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String subscriptionDataId;

    @Column(length=VARCHAR_1000, nullable=false, updatable=false)
    String description;

    public String getChargeDataId() {
        return chargeDataId;
    }

    public void setChargeDataId(String chargeDataId) {
        this.chargeDataId = chargeDataId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubscriptionDataId() {
        return subscriptionDataId;
    }

    public void setSubscriptionDataId(String subscriptionDataId) {
        this.subscriptionDataId = subscriptionDataId;
    }
}
