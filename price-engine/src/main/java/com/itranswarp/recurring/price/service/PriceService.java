package com.itranswarp.recurring.price.service;

import com.itranswarp.recurring.price.PriceInputItem;
import com.itranswarp.recurring.price.processor.PriceProcessor;
import com.itranswarp.recurring.price.processor.PriceProcessorFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class PriceService {

    @Inject
    PriceProcessorFactory priceProcessorFactory;

    public void checkPriceData(String priceType, String priceData) {
        priceProcessorFactory.fetchProcessor(priceType).checkData(priceData);
    }

    public Double calculate(PriceInputItem inputItem) {
        PriceProcessor processor = priceProcessorFactory.fetchProcessor(inputItem.getPriceType());
        return processor.calculate(inputItem);
    }

}
