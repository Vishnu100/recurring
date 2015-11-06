package com.itranswarp.recurring.rating.rest;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.rating.RatingVo;
import com.itranswarp.recurring.rating.service.RatingService;
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
public class RatingController {

    @Inject
    RatingService ratingService;

    @RequestMapping(value = "/api/rating/rate_with_subscription", method = RequestMethod.POST)
    public void rateWithSubscription(@RequestBody RatingVo ratingVo) {
        ratingService.rateWithSubscriptionData(ratingVo.getSubscriptionDataId(),ratingVo.getProcessToDate());
    }


}
