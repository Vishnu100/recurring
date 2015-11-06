package com.itranswarp.recurring.billing.dto;

import com.itranswarp.recurring.billing.model.InvoiceItem;
import com.itranswarp.recurring.rating.model.RatingItem;

/**
 * Created by changsure on 15/9/8.
 */
public class InvoiceItemDto {

    InvoiceItem invoiceItem;

    RatingItem ratingItem;

    public InvoiceItemDto(InvoiceItem invoiceItem, RatingItem ratingItem) {
        this.invoiceItem = invoiceItem;
        this.ratingItem = ratingItem;
    }

    public InvoiceItem getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(InvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public RatingItem getRatingItem() {
        return ratingItem;
    }

    public void setRatingItem(RatingItem ratingItem) {
        this.ratingItem = ratingItem;
    }
}
