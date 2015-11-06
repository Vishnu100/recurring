package com.itranswarp.recurring.subscription.ext.rest;

import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.ext.model.Amendment;
import com.itranswarp.recurring.subscription.ext.service.SubscriptionExtService;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import com.itranswarp.recurring.subscription.vo.SubscriptionVo;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by changsure on 15/8/23.
 */

@RestController
public class SubscriptionExtController {

    @Inject
    SubscriptionExtService subscriptionExtService;

    @Inject
    SubscriptionService subscriptionService;

    @RequestMapping(value = "/api/subscription/subscribe/{processorType}", method = RequestMethod.POST)
    public SubscriptionVo subscribe(@PathVariable(value = "processorType") String processorType, @RequestBody String data) {
        Subscription subscription = subscriptionExtService.subscribe(processorType, data);
        SubscriptionDataDto subscriptionData = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());

        return new SubscriptionVo().build(subscription,subscriptionData);
    }

    @RequestMapping(value = "/api/subscription/amend/{processorType}/{amendType}/{subscriptionId}", method = RequestMethod.POST)
    public Amendment amend(@PathVariable(value = "processorType") String processorType,@PathVariable(value = "amendType") String amendType, @PathVariable(value = "subscriptionId") String subscriptionId, @RequestBody String amendData) {
        Amendment amendment = subscriptionExtService.amend(processorType,amendType,subscriptionId,amendData);
        return amendment;
    }

    @RequestMapping(value = "/api/subscription/amendments", method = RequestMethod.GET)
    public PagedResults<Amendment> readAmendments(@PathVariable(value = "subscriptionId") String subscriptionId, @RequestBody SubscriptionVo subscriptionVo) {
        PagedResults<Amendment> results = subscriptionExtService.readAmendments(subscriptionId);
        return results;
    }

    @RequestMapping(value = "/api/subscription/amendment/{amendmentId}", method = RequestMethod.GET)
    public Amendment readAmendment(@PathVariable(value = "amendmentId") String amendmentId) {
        Amendment amendment = subscriptionExtService.readAmendment(amendmentId);
        return amendment;
    }


}
