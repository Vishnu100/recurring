package com.itranswarp.recurring.subscription.dto;

import com.itranswarp.recurring.product.model.ChargeData;
import com.itranswarp.recurring.subscription.model.SubscriptionCharge;
import com.itranswarp.recurring.subscription.model.SubscriptionChargeSegment;

import java.util.List;

/**
 * Created by changsure on 15/8/15.
 */
public class SubscriptionChargeDto {

    SubscriptionCharge subscriptionCharge;

    ChargeData chargeData;

    List<SubscriptionChargeSegment> subscriptionChargeSegmentList;

    public SubscriptionCharge getSubscriptionCharge() {
        return subscriptionCharge;
    }

    public void setSubscriptionCharge(SubscriptionCharge subscriptionCharge) {
        this.subscriptionCharge = subscriptionCharge;
    }

    public List<SubscriptionChargeSegment> getSubscriptionChargeSegmentList() {
        return subscriptionChargeSegmentList;
    }

    public void setSubscriptionChargeSegmentList(List<SubscriptionChargeSegment> subscriptionChargeSegmentList) {
        this.subscriptionChargeSegmentList = subscriptionChargeSegmentList;
    }

    public ChargeData getChargeData() {
        return chargeData;
    }

    public void setChargeData(ChargeData chargeData) {
        this.chargeData = chargeData;
    }

    public SubscriptionChargeDto build(SubscriptionCharge subscriptionCharge, List<SubscriptionChargeSegment> subscriptionChargeSegmentList,ChargeData chargeData) {
        this.subscriptionCharge = subscriptionCharge;
        this.subscriptionChargeSegmentList = subscriptionChargeSegmentList;
        this.chargeData = chargeData;
        return this;
    }
}
