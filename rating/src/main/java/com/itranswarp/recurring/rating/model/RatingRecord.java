package com.itranswarp.recurring.rating.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itranswarp.recurring.db.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class RatingRecord extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false)
    String subscriptionDataId;

    @Column(columnDefinition = "date")
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

