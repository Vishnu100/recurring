package com.itranswarp.recurring.price.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itranswarp.recurring.price.PriceInputItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by changsure on 15/8/26.
 */
public interface PriceProcessor {

    void checkData(String priceData);

    Double calculate(PriceInputItem inputItem);
}
