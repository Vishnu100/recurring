package com.itranswarp.recurring.billing.service;

import com.itranswarp.recurring.billing.dto.InvoiceItemDto;
import com.itranswarp.recurring.billing.model.Invoice;
import com.itranswarp.recurring.billing.model.InvoiceItem;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.rating.model.RatingItem;
import com.itranswarp.recurring.rating.service.RatingService;
import com.itranswarp.recurring.subscription.dto.SubscriptionDataDto;
import com.itranswarp.recurring.subscription.model.Subscription;
import com.itranswarp.recurring.subscription.service.SubscriptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by changsure on 15/9/7.
 */
@Named
public class BillingService {
    @Inject
    Database database;

    @Inject
    SubscriptionService subscriptionService;

    @Inject
    RatingService ratingService;


    @Transactional
    public Invoice generateInvoice(String subscriptionId, LocalDate invoiceDate) {
        //0. prepare
        Subscription subscription = subscriptionService.read(subscriptionId);

        Invoice latestInvoice = database.from(Invoice.class).where("subscriptionId = ?", subscription.getId()).orderBy("invoiceDate desc").first();
        if (null != latestInvoice && (latestInvoice.getInvoiceDate().isAfter(invoiceDate) || latestInvoice.getInvoiceDate().isEqual(invoiceDate))) {
            if(StringUtils.equals(subscription.getSubscriptionDataId(),latestInvoice.getSubscriptionDataId())){
                return latestInvoice;
            }
        }
        Invoice newInvoice = new Invoice();
        newInvoice.setSubscriptionId(subscription.getId());
        newInvoice.setSubscriptionDataId(subscription.getSubscriptionDataId());
        newInvoice.setInvoiceDate(invoiceDate);
        newInvoice.setStatus(Invoice.InvoiceStatus.DRAFT);
        newInvoice.setAccountId(subscription.getAccountId());

        //0.1 confirm rating ready
        ratingService.rateWithSubscriptionData(subscription.getSubscriptionDataId(), newInvoice.getInvoiceDate());

        //1. fetch
        List<RatingItem> lastRatingItemList = Collections.emptyList();
        if (null != latestInvoice) {
            lastRatingItemList = fetchRatingItemListBySubscriptionData(latestInvoice.getSubscriptionDataId(),latestInvoice.getInvoiceDate());
        }
        List<RatingItem> newRatingItemList = fetchRatingItemListBySubscriptionData(newInvoice.getSubscriptionDataId(),newInvoice.getInvoiceDate());

        //2. compare
        CompareDto compareDto = compare(lastRatingItemList, newRatingItemList);

        //3. collect
        List<InvoiceItem> invoiceItemList = collect(compareDto.getPositiveRatingItemList(), compareDto.getNegativeRatingItemList());

        //4. persistence
        database.save(newInvoice);
        invoiceItemList.stream().forEach((InvoiceItem invoiceItem) -> {
            invoiceItem.setInvoiceId(newInvoice.getId());
            database.save(invoiceItem);
        });

        return newInvoice;
    }

    private List<RatingItem> fetchRatingItemListBySubscriptionData(String subscriptionDataId, LocalDate invoiceDate) {
        List<RatingItem> ratingItemList = database.from(RatingItem.class).where("subscriptionDataId = ?", subscriptionDataId).and("billingDate <= ?",invoiceDate.toString()).list();
        return ratingItemList;
    }

    private CompareDto compare(List<RatingItem> lastRatingItemList, List<RatingItem> newRatingItemList) {
        List<RatingItem> positiveRatingItemList = newRatingItemList.stream().filter((RatingItem ratingItem) -> !lastRatingItemList.contains(ratingItem)).collect(Collectors.toList());
        List<RatingItem> negativeRatingItemList = lastRatingItemList.stream().filter((RatingItem ratingItem) -> !newRatingItemList.contains(ratingItem)).collect(Collectors.toList());
        return new CompareDto(positiveRatingItemList, negativeRatingItemList);
    }

    private List<InvoiceItem> collect(List<RatingItem> positiveRatingItemList, List<RatingItem> negativeRatingItemList) {
        Stream<InvoiceItem> positiveInoInvoiceItemStream = positiveRatingItemList.stream().map((RatingItem ratingItem) -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setIsCredit(false);
            invoiceItem.setRatingItemId(ratingItem.getId());
            invoiceItem.setTotal(ratingItem.getTotalPrice());
            return invoiceItem;
        });

        Stream<InvoiceItem> negativeInoInvoiceItemStream = negativeRatingItemList.stream().map((RatingItem ratingItem) -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setIsCredit(true);
            invoiceItem.setRatingItemId(ratingItem.getId());
            invoiceItem.setTotal(-ratingItem.getTotalPrice());
            return invoiceItem;
        });

        List<InvoiceItem> invoiceItemList = Stream.concat(positiveInoInvoiceItemStream, negativeInoInvoiceItemStream).collect(Collectors.toList());

        return invoiceItemList;
    }

    public Invoice readInvoice(String invoiceId){
        return database.fetch(Invoice.class,invoiceId);
    }

    public List<InvoiceItemDto> readInvoiceItems(String invoiceId){
        List<InvoiceItem> invoiceItemList = database.from(InvoiceItem.class).where("invoiceId = ?",invoiceId).list();
        List<InvoiceItemDto> invoiceItemDtoList = invoiceItemList.stream().map((InvoiceItem invoiceItem)->{
            RatingItem ratingItem = database.fetch(RatingItem.class,invoiceItem.getRatingItemId());
            InvoiceItemDto invoiceItemDto = new InvoiceItemDto(invoiceItem,ratingItem);
            return invoiceItemDto;
        }).collect(Collectors.toList());
        return invoiceItemDtoList;
    }

    public void postInvoice(String invoiceId){
        //TODO: post email or sms or pdf
        Invoice invoice = database.fetch(Invoice.class,invoiceId);
        invoice.setStatus(Invoice.InvoiceStatus.POSTED);
        database.update(invoice);
    }


    class CompareDto {
        List<RatingItem> positiveRatingItemList;
        List<RatingItem> negativeRatingItemList;

        public CompareDto(List<RatingItem> positiveRatingItemList, List<RatingItem> negativeRatingItemList) {
            this.negativeRatingItemList = negativeRatingItemList;
            this.positiveRatingItemList = positiveRatingItemList;
        }

        public List<RatingItem> getNegativeRatingItemList() {
            return negativeRatingItemList;
        }

        public void setNegativeRatingItemList(List<RatingItem> negativeRatingItemList) {
            this.negativeRatingItemList = negativeRatingItemList;
        }

        public List<RatingItem> getPositiveRatingItemList() {
            return positiveRatingItemList;
        }

        public void setPositiveRatingItemList(List<RatingItem> positiveRatingItemList) {
            this.positiveRatingItemList = positiveRatingItemList;
        }
    }




}
