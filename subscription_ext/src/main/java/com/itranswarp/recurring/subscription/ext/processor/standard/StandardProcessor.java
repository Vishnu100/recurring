package com.itranswarp.recurring.subscription.ext.processor.standard;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.product.model.ChargeData;
import com.itranswarp.recurring.product.service.ChargeService;
import com.itranswarp.recurring.subscription.dto.SubscriptionChargeDto;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.ext.processor.ExtProcessor;
import com.itranswarp.recurring.subscription.ext.processor.standard.dto.StandardSubscriptionCustomData;
import com.itranswarp.recurring.subscription.ext.processor.standard.util.CustomDataCalculator;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.model.SubscriptionCharge;
import com.itranswarp.recurring.subscription.model.SubscriptionChargeSegment;
import com.itranswarp.recurring.subscription.model.SubscriptionData;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import com.itranswarp.recurring.subscription.vo.SubscriptionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by changsure on 15/8/15.
 */
@Named
public class StandardProcessor implements ExtProcessor {

    @Inject
    ChargeService chargeService;

    @Inject
    SubscriptionService subscriptionService;

    @Override
    public String subscribe(String data){

        SubscriptionVo subscriptionVo = null;
        try {
            subscriptionVo = JsonUtil.jsonToEntity(data,SubscriptionVo.class);
        } catch (IOException e) {
            throw new APIException("Subscription data format not right, pharse error.",e);
        }

        String customDataString = subscriptionVo.getSubscriptionDataDto().getSubscriptionData().getCustomData();
        StandardSubscriptionCustomData customData = null;
        try {
            customData = JsonUtil.jsonToEntity(customDataString,StandardSubscriptionCustomData.class);
        } catch (IOException e) {
            throw new APIException("Custom data format not right, pharse error.",e);
        }

        SubscriptionData subscriptionData = subscriptionVo.getSubscriptionDataDto().getSubscriptionData();
        subscriptionData.setStartDate(customData.getTermStartDate());
        subscriptionData.setEndDate(CustomDataCalculator.calculateIntialTermEndDate(customData));

        subscriptionVo.getSubscriptionDataDto().getSubscriptionChargeList().stream().forEach((SubscriptionChargeDto chargeDto) -> {
            SubscriptionCharge subscriptionCharge = chargeDto.getSubscriptionCharge();
            ChargeData chargeData = chargeService.readChargeDataById(subscriptionCharge.getChargeDataId());

            SubscriptionChargeSegment subscriptionChargeSegment = new SubscriptionChargeSegment();

            BeanUtils.copyProperties(chargeData, subscriptionChargeSegment);
            subscriptionChargeSegment.setId(null);
            subscriptionChargeSegment.setStartDate(subscriptionData.getStartDate());
            subscriptionChargeSegment.setEndDate(subscriptionData.getEndDate());

            List<SubscriptionChargeSegment> segmentList = new ArrayList();
            segmentList.add(subscriptionChargeSegment);
            chargeDto.setSubscriptionChargeSegmentList(Collections.unmodifiableList(segmentList));
        });


        Subscription subscription = subscriptionService.create(subscriptionVo.getSubscriptionDataDto());
        subscriptionService.active(subscription.getId());

        return subscription.getId();

    }

    @Override
    public void amend(String amendmentType, String subscriptionId, String amendmentData) {
        SubscriptionDataDto dataDto = null;
        if(StringUtils.equalsIgnoreCase(amendmentType,"RENEW")){
            dataDto = doRenewForSubscriptionData(subscriptionId);
        }
        subscriptionService.update(subscriptionId,dataDto);
    }

    private SubscriptionDataDto doRenewForSubscriptionData(String subscriptionId){
        Subscription subscription = subscriptionService.read(subscriptionId);
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscription.getSubscriptionDataId());

        SubscriptionData subscriptionData = subscriptionDataDto.getSubscriptionData();
        subscriptionData.setId(null);

        StandardSubscriptionCustomData customData = null;
        try {
            customData = JsonUtil.jsonToEntity(subscriptionData.getCustomData(),StandardSubscriptionCustomData.class);
        } catch (IOException e) {
            throw new APIArgumentException("initData","Init data format not right, pharse error.");
        }

        subscriptionData.setEndDate(CustomDataCalculator.calculateRenewTermEndDate(subscriptionData.getEndDate().plusDays(1), customData));

        subscriptionDataDto.getSubscriptionChargeList().stream().forEach((SubscriptionChargeDto chargeDto)->{
            chargeDto.getSubscriptionCharge().setId(null);
            chargeDto.getSubscriptionChargeSegmentList().stream().forEach((SubscriptionChargeSegment subscriptionChargeSegment) -> {
                subscriptionChargeSegment.setId(null);
                subscriptionChargeSegment.setEndDate(subscriptionData.getEndDate());
            });
        });
        return subscriptionDataDto;
    }


}
