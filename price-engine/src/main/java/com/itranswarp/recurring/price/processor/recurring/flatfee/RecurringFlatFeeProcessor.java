package com.itranswarp.recurring.price.processor.recurring.flatfee;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.price.PriceInputItem;
import com.itranswarp.recurring.price.processor.PriceProcessor;
import com.itranswarp.recurring.price.processor.recurring.RecurringProcessor;
import org.apache.commons.lang3.StringUtils;

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
public class RecurringFlatFeeProcessor extends RecurringProcessor implements PriceProcessor{
    @Override
    public void checkData(String priceData) {
        RecurringFlatFeeData flatFeeData = null;
        try {
            flatFeeData = JsonUtil.jsonToEntity(priceData, RecurringFlatFeeData.class);
        } catch (IOException e) {
            throw new APIException("Price data format not right, pharse error.", e);
        }
        this.checkData(flatFeeData);
    }

    @Override
    public Double calculate(PriceInputItem inputItem) {

        this.checkData(inputItem.getPriceData());

        RecurringFlatFeeData flatFeeData = null;
        try {
            flatFeeData = JsonUtil.jsonToEntity(inputItem.getPriceData(), RecurringFlatFeeData.class);
        } catch (IOException e) {
            throw new APIException("Price data format not right, pharse error.", e);
        }

        Double result = this.calculate(flatFeeData,inputItem.getStartDate(),inputItem.getEndDate(),inputItem.getIsFullMonth());

        return result;
    }
}
