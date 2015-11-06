package com.itranswarp.recurring.period.processor.full;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;
import com.itranswarp.recurring.period.processor.PeriodProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class FullPeriodProcessor implements PeriodProcessor {

    private final String TIMING_ADVANCE = "ADVANCE";
    private final String TIMING_ARREARS = "ARREARS";

    @Override
    public void checkData(String billingData) {

        FullData fullData = null;
        try {
            fullData = JsonUtil.jsonToEntity(billingData, FullData.class);
        } catch (IOException e) {
            throw new APIException("Billing data format not right, pharse error.", e);
        }

        if (!Arrays.asList(TIMING_ADVANCE, TIMING_ARREARS).contains(fullData.getTiming())) {
            throw new APIArgumentException("billingData-timing", "billingData's timing format not right, pharse error.");
        }
    }

    @Override
    public List<PeriodOutputItem> generatePeriods(PeriodInputItem inputItem) {
        checkData(inputItem.getBillingData());


        if (inputItem.getEndDate().isBefore(inputItem.getCalFromDate()) || inputItem.getStartDate().isAfter(inputItem.getCalToDate())) {
            return Collections.emptyList();
        }

        FullData fullData = null;
        try {
            fullData = JsonUtil.jsonToEntity(inputItem.getBillingData(), FullData.class);
        } catch (IOException e) {
            throw new APIException("Billing data format not right, pharse error.", e);
        }


        PeriodOutputItem outputItem = null;
        LocalDate billingDate = null;
        if (StringUtils.equalsIgnoreCase("ARREARS", fullData.getTiming())) {
            billingDate = inputItem.getEndDate();
        } else {
            billingDate = inputItem.getStartDate();
        }

        if ((billingDate.isBefore(inputItem.getCalToDate()) || billingDate.isEqual(inputItem.getCalToDate()))
                && (billingDate.isEqual(inputItem.getCalFromDate()) || billingDate.isAfter(inputItem.getCalFromDate()))) {
            outputItem = generateFullPeriodItem(inputItem, billingDate);
        }

        if (null == outputItem) {
            return Collections.emptyList();
        } else {
            List<PeriodOutputItem> list = new ArrayList();
            list.add(outputItem);
            return list;
        }
    }

    private PeriodOutputItem generateFullPeriodItem(PeriodInputItem inputItem, LocalDate billingDate) {
        PeriodOutputItem item = new PeriodOutputItem();
        item.setStartDate(inputItem.getStartDate());
        item.setEndDate(inputItem.getEndDate());
        item.setBillingDate(billingDate);

        if (Period.between(item.getStartDate(), item.getEndDate().plusDays(1)).getDays() == 0) {
            item.setIsFullMonth(true);
        } else if (item.getStartDate().with(TemporalAdjusters.lastDayOfMonth()).isEqual(item.getStartDate()) && item.getEndDate().plusDays(1).getDayOfMonth() >= item.getStartDate().getDayOfMonth()) {
            item.setIsFullMonth(true);
        } else if (item.getEndDate().with(TemporalAdjusters.lastDayOfMonth()).isEqual(item.getEndDate().plusDays(1)) && item.getStartDate().getDayOfMonth() >= item.getEndDate().plusDays(1).getDayOfMonth()) {
            item.setIsFullMonth(true);
        } else {
            item.setIsFullMonth(false);
        }

        return item;
    }

}
