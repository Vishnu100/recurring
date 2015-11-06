package com.itranswarp.recurring.subscription.rest;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import com.itranswarp.recurring.subscription.vo.SubscriptionVo;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by changsure on 15/8/23.
 */

@RestController
public class SubscriptionController {

    @Inject
    SubscriptionService subscriptionService;

    @RequestMapping(value = "/api/subscription/subscription", method = RequestMethod.POST)
    public SubscriptionVo createSubscription(@RequestBody SubscriptionVo subscriptionVo) {

        if (subscriptionVo.getSubscriptionDataDto() == null) {
            throw new APIArgumentException("subscriptionDataDto", "Subscription Data Dto can not be null!");
        }
        if (subscriptionVo.getSubscriptionDataDto().getSubscriptionData() == null) {
            throw new APIArgumentException("subscriptionData", "Subscription Data can not be null!");
        }

        Subscription subscription = subscriptionService.create(subscriptionVo.getSubscriptionDataDto());
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());

        subscriptionVo.build(subscription, subscriptionDataDto);
        return subscriptionVo;
    }

    @RequestMapping(value = "/api/subscription/subscription/{subscriptionId}", method = RequestMethod.PUT)
    public SubscriptionVo updateSubscription(@PathVariable(value = "subscriptionId") String subscriptionId, @RequestBody SubscriptionVo subscriptionVo) {

        if (subscriptionVo.getSubscriptionDataDto() == null) {
            throw new APIArgumentException("subscriptionDataDto", "Subscription Data Dto can not be null!");
        }
        if (subscriptionVo.getSubscriptionDataDto().getSubscriptionData() == null) {
            throw new APIArgumentException("subscriptionData", "Subscription Data can not be null!");
        }

        Subscription subscription = subscriptionService.update(subscriptionId, subscriptionVo.getSubscriptionDataDto());
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());

        subscriptionVo.build(subscription, subscriptionDataDto);
        return subscriptionVo;
    }

    @RequestMapping(value = "/api/subscription/subscription/{subscriptionId}", method = RequestMethod.GET)
    public SubscriptionVo readSubscription(@PathVariable(value = "subscriptionId") String subscriptionId) {

        Subscription subscription = subscriptionService.read(subscriptionId);
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());

        SubscriptionVo subscriptionVo = new SubscriptionVo();
        subscriptionVo.build(subscription, subscriptionDataDto);
        return subscriptionVo;
    }

    @RequestMapping(value = "/api/subscription/subscription/{subscriptionId}", method = RequestMethod.DELETE)
    public SubscriptionVo deleteSubscription(@PathVariable(value = "subscriptionId") String subscriptionId) {

        Subscription subscription = subscriptionService.delete(subscriptionId);

        SubscriptionVo subscriptionVo = new SubscriptionVo();
        subscriptionVo.build(subscription, null);
        return subscriptionVo;
    }

    @RequestMapping(value = "/api/subscription/business/active/{subscriptionId}", method = RequestMethod.POST)
    public void activeSubscriptionData(@PathVariable(value = "subscriptionId") String subscriptionId) {
        subscriptionService.active(subscriptionId);
    }

    @RequestMapping(value = "/api/subscription/subscription_data/{subscriptionDataId}", method = RequestMethod.GET)
    public SubscriptionDataDto readSubscriptionData(@PathVariable(value = "subscriptionDataId") String subscriptionDataId){
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscriptionDataId);
        return subscriptionDataDto;
    }


    @RequestMapping(value = "/api/subscription/subscriptions", method = RequestMethod.GET)
    public PagedResults<SubscriptionVo> readSubscriptions(@RequestParam Integer pageIndex,@RequestParam Integer itemsPerPage,@RequestParam String status){
        PagedResults<Subscription> subscriptionPagedResults = subscriptionService.readSubscriptions(status, pageIndex, itemsPerPage);

        PagedResults<SubscriptionVo> subscriptionVoPagedResults = subscriptionPagedResults.map(
                (Subscription subscription) -> {
                    SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());
                    SubscriptionVo subscriptionVo = new SubscriptionVo();
                    subscriptionVo.build(subscription, subscriptionDataDto);
                    return subscriptionVo;
                }
        );

        return subscriptionVoPagedResults;
    }

}
