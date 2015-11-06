package com.itranswarp.recurring.price.processor.recurring;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.price.PriceInputItem;
import com.itranswarp.recurring.price.processor.PriceProcessor;
import com.itranswarp.recurring.price.processor.recurring.flatfee.RecurringFlatFeeData;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class RecurringProcessor {

    protected void checkData(RecurringData priceData) {

        if (null == priceData.getPrice()) {
            throw new APIArgumentException("priceData-price", "billingData's price can not be null.");
        }

        if (StringUtils.isBlank(priceData.getPriceBase())) {
            throw new APIArgumentException("priceData-priceBase", "priceData's price base not right.");
        }

    }

    protected Double calculate(RecurringData priceData,LocalDate startDate,LocalDate endDate,Boolean isFullMonth) {

        this.checkData(priceData);

        BigDecimal result = BigDecimal.valueOf(0);
        if (StringUtils.equalsIgnoreCase("MONTH", priceData.getPriceBase())) {
            BigDecimal dayOfMonth = priceData.getDayOfMonth()==null?BigDecimal.valueOf(30):BigDecimal.valueOf(priceData.getDayOfMonth());
            Period period = Period.between(startDate, endDate.plusDays(1));
            BigDecimal monthGap = BigDecimal.valueOf(period.getYears() * 12 + period.getMonths());
            BigDecimal dayGap = BigDecimal.valueOf(period.getDays());

            if(isFullMonth!=null && isFullMonth){
                if(period.getDays()>0 &&period.getDays()<5){
                    dayGap = BigDecimal.valueOf(0);
                }else if(period.getDays()>25){
                    dayGap = BigDecimal.valueOf(0);
                    monthGap = monthGap.add(BigDecimal.valueOf(1));
                }
            }

            result = monthGap.multiply(BigDecimal.valueOf(priceData.getPrice())).add(dayGap.multiply(BigDecimal.valueOf(priceData.getPrice())).divide(dayOfMonth,2,RoundingMode.HALF_UP));
        } else if (StringUtils.equalsIgnoreCase("YEAR", priceData.getPriceBase())) {
            BigDecimal dayOfYear = priceData.getDayOfYear()==null?BigDecimal.valueOf(365):BigDecimal.valueOf(priceData.getDayOfYear());
            Period period = Period.between(startDate,endDate.plusDays(1));
            BigDecimal yearGap = BigDecimal.valueOf(period.getYears());
            BigDecimal monthGap = BigDecimal.valueOf(period.getMonths());
            BigDecimal dayGap = BigDecimal.valueOf(period.getDays());

            if(isFullMonth!=null && isFullMonth){
                if(period.getDays()>0 &&period.getDays()<5){
                    dayGap = BigDecimal.valueOf(0);
                }else if(period.getDays()>25){
                    dayGap = BigDecimal.valueOf(0);
                    monthGap = monthGap.add(BigDecimal.valueOf(1));
                }
            }

            BigDecimal yearResult = yearGap.multiply(BigDecimal.valueOf(priceData.getPrice()));
            BigDecimal monthResult = monthGap.multiply(BigDecimal.valueOf(priceData.getPrice())).divide(BigDecimal.valueOf(12),2,RoundingMode.HALF_UP);
            BigDecimal dayResult = dayGap.multiply(BigDecimal.valueOf(priceData.getPrice())).divide(dayOfYear, 2, RoundingMode.HALF_UP);
            result = yearResult.add(monthResult).add(dayResult);
        } else if (StringUtils.equalsIgnoreCase("DAY", priceData.getPriceBase())) {
            long dayGap = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
            result = BigDecimal.valueOf(dayGap).multiply((BigDecimal.valueOf(priceData.getPrice())));
        } else if (StringUtils.equalsIgnoreCase("WEEK", priceData.getPriceBase())) {
            long dayGap = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
            result = BigDecimal.valueOf(dayGap).multiply(BigDecimal.valueOf(priceData.getPrice())).divide(BigDecimal.valueOf(7), 3, RoundingMode.HALF_UP);
        }

        return result.setScale(2,RoundingMode.HALF_UP).doubleValue();
    }
}
