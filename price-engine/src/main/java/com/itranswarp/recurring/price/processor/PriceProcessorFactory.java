package com.itranswarp.recurring.price.processor;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.price.processor.recurring.flatfee.RecurringFlatFeeProcessor;
import com.itranswarp.recurring.price.processor.recurring.perunit.RecurringPerUnitProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by changsure on 15/8/26.
 */
@Named
public class PriceProcessorFactory {

    @Inject
    RecurringFlatFeeProcessor recurringFlatFeeProcessor;

    @Inject
    RecurringPerUnitProcessor recurringPerUnitProcessor;

    public PriceProcessor fetchProcessor(String priceType) {
        if (StringUtils.equalsIgnoreCase(priceType, "RECURRING_FLAT_FEE")) {
            return recurringFlatFeeProcessor;
        }else if (StringUtils.equalsIgnoreCase(priceType, "RECURRING_PER_UNIT")) {
            return recurringPerUnitProcessor;
        } else {
            throw new APIArgumentException("priceType", "Processor type " + priceType + " can not found related processor!");
        }
    }


}
