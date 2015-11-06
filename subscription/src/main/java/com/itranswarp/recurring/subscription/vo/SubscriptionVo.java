package com.itranswarp.recurring.subscription.vo;

import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.model.Subscription;

/**
 * Created by changsure on 15/8/15.
 */
public class SubscriptionVo {
    Subscription subscription;

    SubscriptionDataDto subscriptionDataDto;

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public SubscriptionDataDto getSubscriptionDataDto() {
        return subscriptionDataDto;
    }

    public void setSubscriptionDataDto(SubscriptionDataDto subscriptionDataDto) {
        this.subscriptionDataDto = subscriptionDataDto;
    }

    public SubscriptionVo build(Subscription subscription,SubscriptionDataDto subscriptionDataDto){
        this.subscription = subscription;
        this.subscriptionDataDto = subscriptionDataDto;
        return this;
    }
}
