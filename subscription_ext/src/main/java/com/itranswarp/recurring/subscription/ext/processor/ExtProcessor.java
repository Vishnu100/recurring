package com.itranswarp.recurring.subscription.ext.processor;

import com.itranswarp.recurring.subscription.model.Subscription;

/**
 * Created by changsure on 15/8/26.
 */
public interface ExtProcessor {

    public String subscribe(String initData);

    public void amend(String amendmentType, String subscriptionId, String amendmentData);
}
