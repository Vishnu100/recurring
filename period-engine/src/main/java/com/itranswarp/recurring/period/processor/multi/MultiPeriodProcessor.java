package com.itranswarp.recurring.period.processor.multi;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;
import com.itranswarp.recurring.period.processor.PeriodProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by changsure on 15/9/6.
 */
@Named
public class MultiPeriodProcessor implements PeriodProcessor {

    private final String TIMING_ADVANCE = "ADVANCE";
    private final String TIMING_ARREARS = "ARREARS";

    private final String PERIOD_YEAR = "YEAR";
    private final String PERIOD_MONTH = "MONTH";
    private final String PERIOD_WEEK = "WEEK";
    private final String PERIOD_DAY = "DAY";


    @Override
    public void checkData(String billingData) {
        MultiData multiData = null;
        try {
            multiData = JsonUtil.jsonToEntity(billingData, MultiData.class);
        } catch (IOException e) {
            throw new APIException("Billing data format not right, pharse error.", e);
        }

        if (!Arrays.asList(TIMING_ADVANCE, TIMING_ARREARS).contains(multiData.getTiming())) {
            throw new APIArgumentException("billingData-timing", "billingData's timing format not right, pharse error.");
        }

        if (!Arrays.asList(PERIOD_YEAR, PERIOD_MONTH, PERIOD_WEEK, PERIOD_DAY).contains(multiData.getPeriodLengthUnit())) {
            throw new APIArgumentException("billingData-periodLengthUnit", "billingData's periodLengthUnit value not right, pharse error.");
        }

        if (StringUtils.equalsIgnoreCase(PERIOD_YEAR, multiData.getPeriodLengthUnit()) && multiData.getPeriodLength() > 366) {
            throw new APIArgumentException("billingData-periodLength", "billingData's periodLength value can not be bigger than 366, pharse error.");
        }

        if (StringUtils.equalsIgnoreCase(PERIOD_MONTH, multiData.getPeriodLengthUnit()) && multiData.getPeriodLength() > 31) {
            throw new APIArgumentException("billingData-periodLength", "billingData's periodLength value can not be bigger than 31, pharse error.");
        }

        if (StringUtils.equalsIgnoreCase(PERIOD_WEEK, multiData.getPeriodLengthUnit()) && multiData.getPeriodLength() > 7) {
            throw new APIArgumentException("billingData-periodLength", "billingData's periodLength value can not be bigger than 7, pharse error.");
        }

    }

    @Override
    public List<PeriodOutputItem> generatePeriods(PeriodInputItem inputItem) {
        checkData(inputItem.getBillingData());

        MultiData multiData = null;
        try {
            multiData = JsonUtil.jsonToEntity(inputItem.getBillingData(), MultiData.class);
        } catch (IOException e) {
            throw new APIException("Billing data format not right, pharse error.", e);
        }

        List<PeriodOutputItem> outputItemList = new ArrayList();

        LocalDate periodStartDay = inputItem.getStartDate();

        while (periodStartDay.isBefore(inputItem.getEndDate().plusDays(1))) {
            PeriodOutputItem outputItem = null;
            if (StringUtils.equalsIgnoreCase(PERIOD_YEAR, multiData.getPeriodLengthUnit())) {
                outputItem = generateYearPeriodItem(inputItem, multiData, periodStartDay);
            } else if (StringUtils.equalsIgnoreCase(PERIOD_MONTH, multiData.getPeriodLengthUnit())) {
                outputItem = generateMonthPeriodItem(inputItem, multiData, periodStartDay);
            } else if (StringUtils.equalsIgnoreCase(PERIOD_WEEK, multiData.getPeriodLengthUnit())) {
                outputItem = generateWeekPeriodItem(inputItem, multiData, periodStartDay);
            } else if (StringUtils.equalsIgnoreCase(PERIOD_DAY, multiData.getPeriodLengthUnit())) {
                outputItem = generateDayPeriodItem(inputItem, multiData, periodStartDay);
            }

            if (StringUtils.equalsIgnoreCase(TIMING_ARREARS, multiData.getTiming())) {
                outputItem.setBillingDate(outputItem.getEndDate());
            } else {
                outputItem.setBillingDate(outputItem.getStartDate());
            }

            outputItemList.add(outputItem);
            periodStartDay = outputItem.getEndDate().plusDays(1);
        }

        outputItemList = outputItemList.stream().filter((periodOutputItem) -> {
            if ((periodOutputItem.getBillingDate().isEqual(inputItem.getCalFromDate()) || periodOutputItem.getBillingDate().isAfter(inputItem.getCalFromDate())) &&
                    (periodOutputItem.getBillingDate().isEqual(inputItem.getCalToDate()) || periodOutputItem.getBillingDate().isBefore(inputItem.getCalToDate()))) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());


        return outputItemList;

    }

    private PeriodOutputItem generateMonthPeriodItem(PeriodInputItem inputItem, MultiData multiData, LocalDate periodStartDay) {
        PeriodOutputItem outputItem = new PeriodOutputItem();
        LocalDate periodEndDay = null;
        if (periodStartDay.getDayOfMonth() == multiData.getBillingDay()){
            periodEndDay = periodStartDay.plusMonths(multiData.getPeriodLength()).minusDays(1);
            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
                outputItem.setIsFullMonth(false);
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
                outputItem.setIsFullMonth(true);
            }
        }else if(periodStartDay.equals(periodStartDay.with(TemporalAdjusters.lastDayOfMonth())) && periodStartDay.getDayOfMonth() < multiData.getBillingDay()){
            periodEndDay = periodStartDay.plusMonths(multiData.getPeriodLength());
            if(periodEndDay.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() < multiData.getBillingDay()){
                periodEndDay = periodEndDay.with(TemporalAdjusters.lastDayOfMonth()).minusDays(1);
            }else{
                periodEndDay = periodEndDay.withDayOfMonth(multiData.getBillingDay()).minusDays(1);
            }

            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
                outputItem.setIsFullMonth(false);
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
                outputItem.setIsFullMonth(true);
            }

        }else {
            if (periodStartDay.getDayOfMonth() < multiData.getBillingDay()) {
                periodEndDay = periodStartDay.withDayOfMonth(multiData.getBillingDay()).minusDays(1);
            } else {
                periodEndDay = periodStartDay.plusMonths(1).withDayOfMonth(multiData.getBillingDay()).minusDays(1);
            }

            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
                outputItem.setIsFullMonth(false);
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
                outputItem.setIsFullMonth(false);
            }

        }

        return outputItem;
    }

    private PeriodOutputItem generateYearPeriodItem(PeriodInputItem inputItem, MultiData multiData, LocalDate periodStartDay) {
        PeriodOutputItem outputItem = new PeriodOutputItem();
        LocalDate periodEndDay = null;
        if (periodStartDay.getDayOfYear() == multiData.getBillingDay()) {
            periodEndDay = periodStartDay.plusYears(multiData.getPeriodLength()).withDayOfYear(multiData.getBillingDay()).minusDays(1);
            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
                outputItem.setIsFullMonth(false);
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
                outputItem.setIsFullMonth(true);
            }
        } else {
            if (periodStartDay.getDayOfYear() < multiData.getBillingDay()) {
                periodEndDay = periodStartDay.withDayOfYear(multiData.getBillingDay()).minusDays(1);
            } else {
                periodEndDay = periodStartDay.plusYears(1).withDayOfYear(multiData.getBillingDay()).minusDays(1);
            }

            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
                outputItem.setIsFullMonth(false);
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
                outputItem.setIsFullMonth(false);
            }

        }

        return outputItem;
    }

    private PeriodOutputItem generateWeekPeriodItem(PeriodInputItem inputItem, MultiData multiData, LocalDate periodStartDay) {
        PeriodOutputItem outputItem = new PeriodOutputItem();
        LocalDate periodEndDay = null;
        if (periodStartDay.getDayOfWeek().getValue() == multiData.getBillingDay()) {
            periodEndDay = periodStartDay.plusWeeks(multiData.getPeriodLength()).minusDays(1);
            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
            }
        } else {
            if (periodStartDay.getDayOfWeek().getValue() < multiData.getBillingDay()) {
                periodEndDay = periodStartDay.minusDays(multiData.getBillingDay() - periodStartDay.getDayOfWeek().getValue()).minusDays(1);
            } else {
                periodEndDay = periodStartDay.plusDays(7 + multiData.getBillingDay() - periodStartDay.getDayOfWeek().getValue()).minusDays(1);
            }

            if (periodEndDay.isAfter(inputItem.getEndDate())) {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(inputItem.getEndDate());
            } else {
                outputItem.setStartDate(periodStartDay);
                outputItem.setEndDate(periodEndDay);
            }

        }
        outputItem.setIsFullMonth(false);
        return outputItem;
    }

    private PeriodOutputItem generateDayPeriodItem(PeriodInputItem inputItem, MultiData multiData, LocalDate periodStartDay) {
        PeriodOutputItem outputItem = new PeriodOutputItem();
        LocalDate periodEndDay = periodStartDay.plusDays(multiData.getPeriodLength()).minusDays(1);

        if (periodEndDay.isAfter(inputItem.getEndDate())) {
            outputItem.setStartDate(periodStartDay);
            outputItem.setEndDate(inputItem.getEndDate());
        } else {
            outputItem.setStartDate(periodStartDay);
            outputItem.setEndDate(periodEndDay);
        }

        outputItem.setIsFullMonth(false);
        return outputItem;
    }
}
