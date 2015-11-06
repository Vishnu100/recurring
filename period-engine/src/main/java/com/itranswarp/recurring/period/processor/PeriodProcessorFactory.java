package com.itranswarp.recurring.period.processor;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.period.processor.full.FullPeriodProcessor;
import com.itranswarp.recurring.period.processor.multi.MultiPeriodProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by changsure on 15/8/26.
 */
@Named
public class PeriodProcessorFactory {

    @Inject
    MultiPeriodProcessor multiPeriodProcessor;

    @Inject
    FullPeriodProcessor fullPeriodProcessor;

    public final String MULTI_PERIOD = "MULTI_PERIOD";
    public final String FULL_PERIOD = "FULL_PERIOD";

    public PeriodProcessor fetchProcessor(String billingType) {
        if (StringUtils.isBlank(billingType)) {
            return fullPeriodProcessor;
        } else if (StringUtils.equalsIgnoreCase(billingType, MULTI_PERIOD)) {
            return multiPeriodProcessor;
        } else if (StringUtils.equalsIgnoreCase(billingType, FULL_PERIOD)) {
            return fullPeriodProcessor;
        } else {
            throw new APIArgumentException("billingType", "Processor type " + billingType + " can not found related processor!");
        }
    }

}
