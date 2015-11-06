package com.itranswarp.recurring.period.service;

import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;
import com.itranswarp.recurring.period.processor.PeriodProcessor;
import com.itranswarp.recurring.period.processor.PeriodProcessorFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class PeriodService {

    @Inject
    PeriodProcessorFactory periodProcessorFactory;

    public void checkBillingData(String billingType, String billingData){
        periodProcessorFactory.fetchProcessor(billingType).checkData(billingData);
    }

    public List<PeriodOutputItem> generatePeriods(PeriodInputItem inputItem){
        PeriodProcessor processor = periodProcessorFactory.fetchProcessor(inputItem.getBillingType());
        return processor.generatePeriods(inputItem);
    }

}
