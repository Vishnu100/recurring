package com.itranswarp.recurring.period.processor.full;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by changsure on 15/9/14.
 */
public class FullPeriodProcessorTest extends TestCase {

    FullPeriodProcessor fullPeriodProcessor;

    @Before
    public void setUp() {
        fullPeriodProcessor = new FullPeriodProcessor();
    }

    @Test
    public void testAdvanceAhead() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(0, periodOutputItemList.size());
    }

    @Test
    public void testAdvanceAheadEndEqualStart() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 1, 15), LocalDate.of(2015, 2, 2), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(0, periodOutputItemList.size());
    }

    @Test
    public void testAdvanceAheadCross() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 1, 15), LocalDate.of(2015, 2, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(0, periodOutputItemList.size());
    }

    @Test
    public void testAdvanceStartEqualAndEndIn() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 2), LocalDate.of(2015, 2, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 2, 15), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 2), LocalDate.of(2015, 2, 28), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceStartEqualAndEndEqual() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 2), LocalDate.of(2015, 3, 1), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getBillingDate());
        assertEquals(true, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceStartEqualAndEndCross() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 2), LocalDate.of(2015, 3, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 15), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 2), LocalDate.of(2015, 3, 31), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 31), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 2), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceIn() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 10), LocalDate.of(2015, 2, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 2, 15), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceStartInEndEqual() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 10), LocalDate.of(2015, 3, 1), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 5), LocalDate.of(2015, 3, 1), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 5), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 5), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceStartInEndCross() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 10), LocalDate.of(2015, 3, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 15), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 10), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 28), LocalDate.of(2015, 3, 30), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 28), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 30), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), outputItem.getBillingDate());
        assertEquals(true, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 2, 28), LocalDate.of(2015, 3, 28), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 2, 28), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 28), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), outputItem.getBillingDate());
        assertEquals(true, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceStartEqualEnd() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;
        PeriodOutputItem outputItem;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 3, 1), LocalDate.of(2015, 3, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 3, 15), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getBillingDate());
        assertEquals(false, outputItem.getIsFullMonth().booleanValue());

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 3, 1), LocalDate.of(2015, 4, 30), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());
        outputItem = periodOutputItemList.get(0);
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getStartDate());
        assertEquals(LocalDate.of(2015, 4, 30), outputItem.getEndDate());
        assertEquals(LocalDate.of(2015, 3, 1), outputItem.getBillingDate());
        assertEquals(true, outputItem.getIsFullMonth().booleanValue());

    }

    @Test
    public void testAdvanceBehind() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generatePeriodInputItem(LocalDate.of(2015, 3, 2), LocalDate.of(2015, 3, 15), "ADVANCE");
        periodOutputItemList = fullPeriodProcessor.generatePeriods(inputItem);
        assertEquals(0, periodOutputItemList.size());
    }


    public PeriodInputItem generatePeriodInputItem(LocalDate chargeStartDate, LocalDate chargeEndDate, String timing) {
        FullData fullData = new FullData();
        fullData.setTiming(timing);
        String fullDataString = null;
        try {
            fullDataString = JsonUtil.entityToJson(fullData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PeriodInputItem item = new PeriodInputItem();
        item.setCalFromDate(LocalDate.of(2015, 2, 2));
        item.setCalToDate(LocalDate.of(2015, 3, 1));
        item.setStartDate(chargeStartDate);
        item.setEndDate(chargeEndDate);
        item.setBillingType("FULL_PERIOD");
        item.setBillingData(fullDataString);

        return item;
    }

}