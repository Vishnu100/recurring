package com.itranswarp.recurring.billing.rest;

import com.itranswarp.recurring.billing.dto.InvoiceItemDto;
import com.itranswarp.recurring.billing.model.Invoice;
import com.itranswarp.recurring.billing.model.InvoiceItem;
import com.itranswarp.recurring.billing.service.BillingService;
import com.itranswarp.recurring.billing.vo.BillingVo;
import com.itranswarp.recurring.billing.vo.InvoiceVo;
import com.itranswarp.recurring.rating.RatingVo;
import com.itranswarp.recurring.rating.service.RatingService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by changsure on 15/8/23.
 */

@RestController
public class BillingController {

    @Inject
    BillingService billingService;

    @RequestMapping(value = "/api/billing/generate_invoice", method = RequestMethod.POST)
    public InvoiceVo generateInvoice(@RequestBody BillingVo billingVo) {
        Invoice invoice = billingService.generateInvoice(billingVo.getSubscriptionId(), billingVo.getInvoiceDate());
        List<InvoiceItemDto> invoiceItemDtoList = billingService.readInvoiceItems(invoice.getId());

        return new InvoiceVo(invoice, invoiceItemDtoList);
    }

    @RequestMapping(value = "/api/billing/invoice/{invoiceId}", method = RequestMethod.GET)
    public InvoiceVo readInvoice(@PathVariable(value = "invoiceId") String invoiceId) {
        Invoice invoice = billingService.readInvoice(invoiceId);
        List<InvoiceItemDto> invoiceItemDtoList = billingService.readInvoiceItems(invoice.getId());

        return new InvoiceVo(invoice, invoiceItemDtoList);
    }

}
