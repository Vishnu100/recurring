package com.itranswarp.recurring.subscription.ext.processor;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.subscription.ext.processor.standard.StandardProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by changsure on 15/8/26.
 */
@Named
public class SubscriptionExtProcessorFactory {

    @Inject
    StandardProcessor standardProcessor;


    public ExtProcessor fetchExtProcessor(String processorType) {
        if (StringUtils.isBlank(processorType)) {
            return standardProcessor;
        } else if (StringUtils.equalsIgnoreCase(processorType, "standard")) {
            return standardProcessor;
        } else {
            throw new APIArgumentException("processorType", "Processor type "+processorType + " can not found related processor!");
        }
    }


}
