package com.itranswarp.recurring.subscription.dto;

import com.itranswarp.recurring.subscription.model.SubscriptionData;

import java.util.List;

/**
 * Created by changsure on 15/8/15.
 */
public class SubscriptionDataDto {

    SubscriptionData subscriptionData;

    List<SubscriptionChargeDto> subscriptionChargeList;

    public SubscriptionData getSubscriptionData() {
        return subscriptionData;
    }

    public void setSubscriptionData(SubscriptionData subscriptionData) {
        this.subscriptionData = subscriptionData;
    }

    public List<SubscriptionChargeDto> getSubscriptionChargeList() {
        return subscriptionChargeList;
    }

    public void setSubscriptionChargeList(List<SubscriptionChargeDto> subscriptionChargeList) {
        this.subscriptionChargeList = subscriptionChargeList;
    }

    public SubscriptionDataDto build(SubscriptionData subscriptionData, List<SubscriptionChargeDto> subscriptionChargeList) {
        this.subscriptionData = subscriptionData;
        this.subscriptionChargeList = subscriptionChargeList;
        return this;
    }
}
