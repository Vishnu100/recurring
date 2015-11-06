package com.itranswarp.recurring.billing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itranswarp.recurring.db.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class InvoiceItem extends BaseEntity {

    @Column(length = ID_LENGTH, nullable = false)
    String invoiceId;

    @Column(length = ID_LENGTH, nullable = false)
    String ratingItemId;

    @Column
    Boolean isCredit;

    @Column
    Double total;


    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getRatingItemId() {
        return ratingItemId;
    }

    public void setRatingItemId(String ratingItemId) {
        this.ratingItemId = ratingItemId;
    }

    public Boolean getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

}

