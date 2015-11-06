package com.itranswarp.recurring.billing.vo;

import com.itranswarp.recurring.billing.dto.InvoiceItemDto;
import com.itranswarp.recurring.billing.model.Invoice;

import java.util.List;

/**
 * Created by changsure on 15/9/8.
 */
public class InvoiceVo {
    Invoice invoice;
    List<InvoiceItemDto> invoiceItemDtoList;

    public InvoiceVo(Invoice invoice, List<InvoiceItemDto> invoiceItemDtoList) {
        this.invoice = invoice;
        this.invoiceItemDtoList = invoiceItemDtoList;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<InvoiceItemDto> getInvoiceItemDtoList() {
        return invoiceItemDtoList;
    }

    public void setInvoiceItemDtoList(List<InvoiceItemDto> invoiceItemDtoList) {
        this.invoiceItemDtoList = invoiceItemDtoList;
    }
}
