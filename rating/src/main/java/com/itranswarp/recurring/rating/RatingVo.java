package com.itranswarp.recurring.rating;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Created by changsure on 15/9/7.
 */
public class RatingVo {

    String subscriptionDataId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate processToDate;

    public String getSubscriptionDataId() {
        return subscriptionDataId;
    }

    public void setSubscriptionDataId(String subscriptionDataId) {
        this.subscriptionDataId = subscriptionDataId;
    }

    public LocalDate getProcessToDate() {
        return processToDate;
    }

    public void setProcessToDate(LocalDate processToDate) {
        this.processToDate = processToDate;
    }
}
