package com.itranswarp.recurring.subscription.ext.service;

import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.subscription.ext.model.Amendment;
import com.itranswarp.recurring.subscription.ext.processor.ExtProcessor;
import com.itranswarp.recurring.subscription.ext.processor.SubscriptionExtProcessorFactory;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SubscriptionExtService {

    @Inject
    SubscriptionService subscriptionService;

    @Inject
    SubscriptionExtProcessorFactory subscriptionExtProcessorFactory;

    @Inject
    Database database;

    /**
     * Initialize subscription's subscriptionChargeSegment by initMethod
     * @return
     */
    @Transactional
    public Subscription subscribe(String processorType, String data) {
        ExtProcessor processor = subscriptionExtProcessorFactory.fetchExtProcessor(processorType);
        String subscriptionId = processor.subscribe(data);

        Subscription subscription = subscriptionService.read(subscriptionId);
        return subscription;
    }


    /**
     * Amend subscription, build a new subscriptionData by amendment
     * @param subscriptionId
     * @param amendData
     * @return
     */
    @Transactional
    public Amendment amend(String processorType,String amendmentType, String subscriptionId, String amendData) {

        Subscription subscription = subscriptionService.read(subscriptionId);

        Amendment amendment = new Amendment();
        amendment.setSubscriptionId(subscription.getId());
        amendment.setType(amendmentType);
        amendment.setPrevSubscriptionDataId(subscription.getSubscriptionDataId());
        amendment.setData(amendData);

        ExtProcessor processor = subscriptionExtProcessorFactory.fetchExtProcessor(processorType);
        processor.amend(amendmentType, subscriptionId, amendData);

        subscription = subscriptionService.read(subscriptionId);
        amendment.setNextSubscriptionDataId(subscription.getSubscriptionDataId());
        database.save(amendment);

        return amendment;
    }

    public Amendment readAmendment(String amendmentId){
        Amendment amendment = database.fetch(Amendment.class, amendmentId);
        return amendment;
    }

    public PagedResults<Amendment> readAmendments(String subscriptionId){
        PagedResults<Amendment> results = database.from(Amendment.class).where("subscriptionId= ?").list(0,100);
        return results;
    }

}
