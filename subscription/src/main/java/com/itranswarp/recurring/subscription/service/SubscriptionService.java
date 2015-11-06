package com.itranswarp.recurring.subscription.service;

import com.itranswarp.recurring.account.model.Account;
import com.itranswarp.recurring.account.service.AccountService;
import com.itranswarp.recurring.base.util.BeanUtil;
import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.period.service.PeriodService;
import com.itranswarp.recurring.price.service.PriceService;
import com.itranswarp.recurring.product.model.ChargeData;
import com.itranswarp.recurring.subscription.dto.SubscriptionChargeDto;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class SubscriptionService {

    @Inject
    Database database;

    @Inject
    PriceService priceService;

    @Inject
    PeriodService periodService;

    @Inject
    AccountService accountService;


    @Transactional
    public Subscription create(SubscriptionDataDto subscriptionDataDto) {
        this.validSaveSubscriptionData(subscriptionDataDto);

        Subscription subscription = new Subscription();
        subscription.setSubscriptionDataId("");
        subscription.setStatus(Subscription.SubscriptionStatus.DRAFT);
        subscription.setAccountId("");
        database.save(subscription);

        SubscriptionData subscriptionData = createSubscriptionData(subscription, subscriptionDataDto);

        subscription.setSubscriptionDataId(subscriptionData.getId());
        subscription.setAccountId(subscriptionData.getAccountId());
        database.update(subscription);

        return subscription;

    }

    @Transactional
    public Subscription update(String subscriptionId, SubscriptionDataDto subscriptionDataDto) {

        this.validSaveSubscriptionData(subscriptionDataDto);

        Subscription subscription = database.fetch(Subscription.class, subscriptionId);

        SubscriptionData subscriptionData = createSubscriptionData(subscription, subscriptionDataDto);

        subscription.setSubscriptionDataId(subscriptionData.getId());
        subscription.setAccountId(subscriptionData.getAccountId());
        database.update(subscription);

        return subscription;
    }

    private SubscriptionData createSubscriptionData(Subscription subscription, SubscriptionDataDto subscriptionDataDto) {
        SubscriptionData subscriptionData = subscriptionDataDto.getSubscriptionData();
        subscriptionData.setSubscriptionId(subscription.getId());
        subscriptionData.setPreviousId(StringUtils.trimToEmpty(subscription.getSubscriptionDataId()));
        database.save(subscriptionData);

        subscriptionDataDto.getSubscriptionChargeList().stream().forEach((SubscriptionChargeDto chargeDto) -> {
            SubscriptionCharge subscriptionCharge = chargeDto.getSubscriptionCharge();
            subscriptionCharge.setSubscriptionDataId(subscriptionData.getId());
            database.save(subscriptionCharge);

            ChargeData chargeData = database.fetch(ChargeData.class, subscriptionCharge.getChargeDataId());

            chargeDto.getSubscriptionChargeSegmentList().stream().forEach((SubscriptionChargeSegment segment) -> {
                segment.setSubscriptionChargeId(subscriptionCharge.getId());
                segment.setChargeDataId(subscriptionCharge.getChargeDataId());
                BeanUtils.copyProperties(chargeData, segment, BeanUtil.getNotNullPropertyNames(segment));
                segment.setId(null);
                database.save(segment);
            });
        });

        return subscriptionData;
    }

    public Subscription read(String subscriptionId) {
        Subscription subscription = database.fetch(Subscription.class, subscriptionId);
        return subscription;
    }

    public SubscriptionDataDto readSubscriptionData(String subscriptionDataId) {
        SubscriptionData subscriptionData = database.fetch(SubscriptionData.class, subscriptionDataId);

        List<SubscriptionCharge> subscriptionChargeList = database.from(SubscriptionCharge.class).where(" subscriptionDataId = ? ", subscriptionDataId).list();

        List<SubscriptionChargeDto> subscriptionChargeDtoList = subscriptionChargeList.stream().map((SubscriptionCharge subscriptionCharge) -> {
            List<SubscriptionChargeSegment> chargeSegmentList = database.from(SubscriptionChargeSegment.class).where(" subscriptionChargeId = ? ", subscriptionCharge.getId()).list();
            ChargeData chargeData = database.fetch(ChargeData.class, subscriptionCharge.getChargeDataId());

            SubscriptionChargeDto chargeDto = new SubscriptionChargeDto().build(subscriptionCharge, chargeSegmentList, chargeData);
            return chargeDto;

        }).collect(Collectors.toList());

        SubscriptionDataDto dto = new SubscriptionDataDto().build(subscriptionData, subscriptionChargeDtoList);
        return dto;
    }

    @Transactional
    public Subscription delete(String subscriptionId) {
        Subscription subscription = database.fetch(Subscription.class, subscriptionId);
        subscription.setStatus(Subscription.SubscriptionStatus.DELETE);
        database.update(subscription);

        return subscription;
    }

    private void validSaveSubscriptionData(SubscriptionDataDto subscriptionDataDto) {

        LocalDate subscriptionStartDate = subscriptionDataDto.getSubscriptionData().getStartDate();
        LocalDate subscriptionEndDate = subscriptionDataDto.getSubscriptionData().getEndDate();

        if (subscriptionStartDate == null) {
            throw new APIArgumentException("subscriptionData-startDate", "subscription's start date can not be null.");
        }
        if (subscriptionEndDate == null) {
            throw new APIArgumentException("subscriptionData-endDate", "subscription's end date can not be null.");
        }

        if (StringUtils.isBlank(subscriptionDataDto.getSubscriptionData().getAccountId())) {
            throw new APIArgumentException("subscriptionData-accountId", "subscription's account id can not be null.");
        }

        Account account = accountService.readAccount(subscriptionDataDto.getSubscriptionData().getAccountId());
        if (account == null) {
            throw new APIArgumentException("subscriptionData-accountId", "subscription's account not exist.");
        } else if (StringUtils.equalsIgnoreCase(account.getStatus(), Account.AccountStatus.EXPIRED)) {
            throw new APIArgumentException("subscriptionData-accountId", "subscription's account expired.");
        }

        subscriptionDataDto.getSubscriptionChargeList().stream().forEach((SubscriptionChargeDto subscriptionChargeDto) -> {
            subscriptionChargeDto.getSubscriptionChargeSegmentList().stream().forEach((SubscriptionChargeSegment subscriptionChargeSegment) -> {
                if (subscriptionChargeSegment.getStartDate() == null || subscriptionChargeSegment.getStartDate().isBefore(subscriptionStartDate)) {
                    throw new APIArgumentException("subscriptionChargeSegment-startDate", "subscription charge segment's start date can not be null or before subscription's start date.");
                }
                if (subscriptionChargeSegment.getEndDate() == null || subscriptionChargeSegment.getEndDate().isAfter(subscriptionEndDate)) {
                    throw new APIArgumentException("subscriptionChargeSegment-endDate", "subscription charge segment's end date can not be null or after subscription's end date.");
                }

                if ((null != subscriptionChargeSegment.getBillingType() && null == subscriptionChargeSegment.getBillingData())
                        || (null == subscriptionChargeSegment.getBillingType() && null != subscriptionChargeSegment.getBillingData())
                        ) {
                    throw new APIArgumentException("subscriptionChargeSegment-billingData", "subscription charge segment's billing type and billing data must be all null or all have value.");
                }

                if ((null == subscriptionChargeSegment.getPriceType() && null != subscriptionChargeSegment.getPriceData())
                        || (null != subscriptionChargeSegment.getPriceType() && null == subscriptionChargeSegment.getPriceData())
                        ) {
                    throw new APIArgumentException("subscriptionChargeSegment-priceData", "subscription charge segment's billing type and billing data must be all null or all have value.");
                }

                if (null != subscriptionChargeSegment.getBillingType() && null != subscriptionChargeSegment.getBillingData()) {
                    periodService.checkBillingData(subscriptionChargeSegment.getBillingType(), subscriptionChargeSegment.getBillingData());
                }
                if (null != subscriptionChargeSegment.getPriceType() && null != subscriptionChargeSegment.getPriceData()) {
                    priceService.checkPriceData(subscriptionChargeSegment.getPriceType(), subscriptionChargeSegment.getPriceData());
                }
            });
        });
    }

    @Transactional
    public void active(String subscriptionId) {
        Subscription subscription = database.fetch(Subscription.class, subscriptionId);

        subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        database.update(subscription);

        //send message
    }

    public PagedResults<Subscription> readSubscriptions(String status, Integer pageIndex, Integer itemsPerPage) {
        PagedResults<Subscription> subscriptionList = null;
        if (StringUtils.isEmpty(status)) {
            subscriptionList = database.from(Subscription.class).list(pageIndex, itemsPerPage);
        } else {
            subscriptionList = database.from(Subscription.class).where("status=?", status).list(pageIndex, itemsPerPage);
        }
        return subscriptionList;
    }


}
