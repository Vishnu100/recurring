package com.itranswarp.recurring.rating.service;

import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;
import com.itranswarp.recurring.period.service.PeriodService;
import com.itranswarp.recurring.price.PriceInputItem;
import com.itranswarp.recurring.price.service.PriceService;
import com.itranswarp.recurring.rating.model.RatingItem;
import com.itranswarp.recurring.rating.model.RatingRecord;
import com.itranswarp.recurring.subscription.dto.SubscriptionChargeDto;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.model.SubscriptionChargeSegment;
import com.itranswarp.recurring.subscription.model.SubscriptionData;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by changsure on 15/9/7.
 */
@Named
public class RatingService {

    @Inject
    SubscriptionService subscriptionService;

    @Inject
    PeriodService periodService;

    @Inject
    PriceService priceService;

    @Inject
    Database database;

    @Transactional
    public void rateWithSubscriptionData(String subscriptionDataId, LocalDate processToDate) {

        //0. prepare
        SubscriptionDataDto subscriptionDataDto = subscriptionService.readSubscriptionData(subscriptionDataId);
        Subscription subscription = subscriptionService.read(subscriptionDataDto.getSubscriptionData().getSubscriptionId());
        if(!StringUtils.equalsIgnoreCase(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus())){
            throw new APIException("business:subscription:not_active",null,"Subscription not activated, can not bill on it.");
        }

        RatingRecord ratingRecord = fetchLastProcessToDate(subscriptionDataDto.getSubscriptionData());
        if (ratingRecord.getProcessToDate().isEqual(processToDate)) {
            return;
        }

        //1. fetch
        LocalDate processFromDate = ratingRecord.getProcessToDate().plusDays(1);
        if (subscriptionDataDto.getSubscriptionData().getEndDate().isBefore(processToDate)) {
            processToDate = subscriptionDataDto.getSubscriptionData().getEndDate();
        }
        List<SubscriptionChargeSegment> subscriptionChargeSegmentList = fetchSegments(subscriptionDataDto, processFromDate, processToDate);

        //2. period
        List<RatingItem> ratingItemList = periods(subscriptionDataDto.getSubscriptionData().getId(), subscriptionChargeSegmentList, processFromDate, processToDate);

        //3. price
        ratingItemList = price(ratingItemList);

        //4. persistence
        for (RatingItem ratingItem : ratingItemList) {
            database.save(ratingItem);
        }
        ratingRecord.setProcessToDate(processToDate);
        if (null == ratingRecord.getId()) {
            database.save(ratingRecord);
        } else {
            database.update(ratingRecord);
        }
    }

    private RatingRecord fetchLastProcessToDate(SubscriptionData subscriptionData) {
        List<RatingRecord> ratingRecordList = database.from(RatingRecord.class).where("subscriptionDataId=?", subscriptionData.getId()).list();
        RatingRecord ratingRecord = null;
        if (ratingRecordList.size() == 0) {
            ratingRecord = new RatingRecord();
            ratingRecord.setProcessToDate(subscriptionData.getStartDate().minusDays(1));
            ratingRecord.setSubscriptionDataId(subscriptionData.getId());

        } else {
            ratingRecord = ratingRecordList.get(0);
        }
        return ratingRecord;
    }

    private List<SubscriptionChargeSegment> fetchSegments(SubscriptionDataDto subscriptionDataDto, LocalDate processFrom, LocalDate processTo) {
        List<SubscriptionChargeSegment> segmentList = new LinkedList<>();

        subscriptionDataDto.getSubscriptionChargeList().stream().forEach((SubscriptionChargeDto chargeDto) -> {
            segmentList.addAll(chargeDto.getSubscriptionChargeSegmentList());
        });

        List<SubscriptionChargeSegment> filterSegmentList = segmentList.stream().filter((SubscriptionChargeSegment segment) -> {
            if (segment.getEndDate().isBefore(processFrom) || segment.getStartDate().isAfter(processTo)) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        return filterSegmentList;
    }

    private List<RatingItem> periods(String subscriptionDataId, List<SubscriptionChargeSegment> segmentList, LocalDate processFrom, LocalDate processTo) {
        List<RatingItem> ratingItemList = segmentList.stream().flatMap((SubscriptionChargeSegment segment) -> {
            PeriodInputItem periodInputIterm = new PeriodInputItem();
            BeanUtils.copyProperties(segment, periodInputIterm);
            periodInputIterm.setCalFromDate(processFrom);
            periodInputIterm.setCalToDate(processTo);

            List<PeriodOutputItem> periodOutputItemList = periodService.generatePeriods(periodInputIterm);

            Stream<RatingItem> ratingItemStream = periodOutputItemList.stream().map((PeriodOutputItem periodOutputItem) -> {
                RatingItem ratingItem = new RatingItem();
                BeanUtils.copyProperties(segment, ratingItem);
                ratingItem.setId(null);
                ratingItem.setSubscriptionDataId(subscriptionDataId);
                ratingItem.setSubscriptionChargeSegmentId(segment.getId());
                ratingItem.setStartDate(periodOutputItem.getStartDate());
                ratingItem.setEndDate(periodOutputItem.getEndDate());
                ratingItem.setBillingDate(periodOutputItem.getBillingDate());

                return ratingItem;
            });

            return ratingItemStream;
        }).collect(Collectors.toList());

        return ratingItemList;
    }

    private List<RatingItem> price(List<RatingItem> ratingItemList) {
        ratingItemList.stream().forEach((RatingItem ratingItem) -> {
            PriceInputItem priceInputItem = new PriceInputItem();
            BeanUtils.copyProperties(ratingItem, priceInputItem);

            Double total = priceService.calculate(priceInputItem);
            ratingItem.setTotalPrice(total);
        });

        return ratingItemList;
    }

}
