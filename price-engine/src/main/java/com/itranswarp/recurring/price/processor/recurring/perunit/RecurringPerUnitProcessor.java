package com.itranswarp.recurring.price.processor.recurring.perunit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.price.PriceInputItem;
import com.itranswarp.recurring.price.processor.PriceProcessor;
import com.itranswarp.recurring.price.processor.recurring.RecurringProcessor;
import com.itranswarp.recurring.price.processor.recurring.flatfee.RecurringFlatFeeData;
import com.itranswarp.recurring.price.processor.recurring.flatfee.RecurringFlatFeeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class RecurringPerUnitProcessor extends RecurringProcessor implements PriceProcessor {

    @Override
    public void checkData(String priceData) {
        RecurringPerUnitData perUnitData = null;
        try {
            perUnitData = JsonUtil.jsonToEntity(priceData, RecurringPerUnitData.class);
        } catch (IOException e) {
            throw new APIException("Price data format not right, pharse error.", e);
        }

        if (null == perUnitData.getUnitPrice()) {
            throw new APIArgumentException("priceData-unitPrice", "priceData's unitPrice can not be null.");
        }

        if (null == perUnitData.getQuantity()) {
            throw new APIArgumentException("priceData-quantity", "priceData's quantity base not right.");
        }

        this.checkData(perUnitData);

    }

    @Override
    public Double calculate(PriceInputItem inputItem) {

        this.checkData(inputItem.getPriceData());

        RecurringPerUnitData perUnitData = null;
        try {
            perUnitData = JsonUtil.jsonToEntity(inputItem.getPriceData(), RecurringPerUnitData.class);
        } catch (IOException e) {
            throw new APIException("Price data format not right, pharse error.", e);
        }

        Double result = this.calculate(perUnitData,inputItem.getStartDate(),inputItem.getEndDate(),inputItem.getIsFullMonth());

        return result;
    }
}
