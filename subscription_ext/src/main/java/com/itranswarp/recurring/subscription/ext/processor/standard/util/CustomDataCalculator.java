package com.itranswarp.recurring.subscription.ext.processor.standard.util;

import com.itranswarp.recurring.subscription.ext.processor.standard.dto.StandardSubscriptionCustomData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

/**
 * Created by changsure on 15/8/27.
 */
public class CustomDataCalculator {

    public static LocalDate calculateIntialTermEndDate(StandardSubscriptionCustomData data){
        if(StringUtils.equalsIgnoreCase(data.getTermSetting(), "EVERGREEN")){
            return LocalDate.of(2100,1,1);
        }else{
            if(StringUtils.equalsIgnoreCase(data.getTermUnit(),"DAY")){
                return data.getTermStartDate().plusDays(data.getInitialTerm()).minusDays(1);
            }else if(StringUtils.equalsIgnoreCase(data.getTermUnit(),"WEEK")){
                return data.getTermStartDate().plusWeeks(data.getInitialTerm()).minusDays(1);
            }else{
                return data.getTermStartDate().plusMonths(data.getInitialTerm()).minusDays(1);
            }
        }
    }

    public static LocalDate calculateRenewTermEndDate(LocalDate currentTermEndDate, StandardSubscriptionCustomData data){
        if(StringUtils.equalsIgnoreCase(data.getTermSetting(), "EVERGREEN")){
            return LocalDate.of(2100,1,1);
        }else{
            if(StringUtils.equalsIgnoreCase(data.getTermUnit(),"DAY")){
                return currentTermEndDate.plusDays(data.getRenewalTerm()).minusDays(1);
            }else if(StringUtils.equalsIgnoreCase(data.getTermUnit(),"WEEK")){
                return currentTermEndDate.plusWeeks(data.getRenewalTerm()).minusDays(1);
            }else{
                return currentTermEndDate.plusMonths(data.getRenewalTerm()).minusDays(1);
            }
        }
    }

}
