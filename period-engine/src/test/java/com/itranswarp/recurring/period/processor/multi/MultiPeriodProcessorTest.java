package com.itranswarp.recurring.period.processor.multi;

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
 * Created by changsure on 15/9/16.
 */
public class MultiPeriodProcessorTest extends TestCase {

    MultiPeriodProcessor multiPeriodProcessor;

    @Before
    public void setUp() {
        multiPeriodProcessor = new MultiPeriodProcessor();
    }


    @Test
    public void testMonthAlign0() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2014, 12, 14), LocalDate.of(2015, 1, 31), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

    }

    @Test
    public void testMonthAlign0Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2014, 12, 14), LocalDate.of(2015, 1, 31), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

    }

    @Test
    public void testMonthAlign1() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 31), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

    }

    @Test
    public void testMonthAlign1Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 31), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

    }

    @Test
    public void testMonthAlign2() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(12, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getStartDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getEndDate());
        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getBillingDate());
        assertTrue(periodOutputItemList.get(11).getIsFullMonth());

    }

    @Test
    public void testMonthAlign2Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(12, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getStartDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getEndDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getBillingDate());
        assertTrue(periodOutputItemList.get(11).getIsFullMonth());

    }

    @Test
    public void testMonthAlign3() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(14, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getStartDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getEndDate());
        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getBillingDate());
        assertTrue(periodOutputItemList.get(11).getIsFullMonth());

        assertEquals(LocalDate.of(2016, 2, 1), periodOutputItemList.get(13).getStartDate());
        assertEquals(LocalDate.of(2016, 2, 29), periodOutputItemList.get(13).getEndDate());
        assertEquals(LocalDate.of(2016, 2, 1), periodOutputItemList.get(13).getBillingDate());
        assertTrue(periodOutputItemList.get(13).getIsFullMonth());

    }

    @Test
    public void testMonthAlign3Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2016, 12, 31), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(14, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 12, 1), periodOutputItemList.get(11).getStartDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getEndDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(11).getBillingDate());
        assertTrue(periodOutputItemList.get(11).getIsFullMonth());

        assertEquals(LocalDate.of(2016, 2, 1), periodOutputItemList.get(13).getStartDate());
        assertEquals(LocalDate.of(2016, 2, 29), periodOutputItemList.get(13).getEndDate());
        assertEquals(LocalDate.of(2016, 2, 29), periodOutputItemList.get(13).getBillingDate());
        assertTrue(periodOutputItemList.get(13).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign1() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 3, 14), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 3, 1), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 3, 1), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign1Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 3, 14), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 3, 1), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign2() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 3, 14), "ADVANCE", 1, 30);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign2Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2015, 3, 14), "ARREARS", 1, 30);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 3, 14), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign3() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 6, 30), "ADVANCE", 1, 30);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(7, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 29), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getBillingDate());
        assertTrue(periodOutputItemList.get(2).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 3, 30), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 3, 30), periodOutputItemList.get(3).getBillingDate());
        assertTrue(periodOutputItemList.get(3).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getStartDate());
        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getEndDate());
        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getBillingDate());
        assertFalse(periodOutputItemList.get(6).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign3Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 6, 30), "ARREARS", 1, 30);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(7, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 29), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 27), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 28), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 3, 29), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 3, 29), periodOutputItemList.get(2).getBillingDate());
        assertTrue(periodOutputItemList.get(2).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 3, 30), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(3).getBillingDate());
        assertTrue(periodOutputItemList.get(3).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getStartDate());
        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getEndDate());
        assertEquals(LocalDate.of(2015, 6, 30), periodOutputItemList.get(6).getBillingDate());
        assertFalse(periodOutputItemList.get(6).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign4() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2016, 5, 14), "ADVANCE", 3, 31);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(6, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 4, 30), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 7, 30), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 4, 30), periodOutputItemList.get(2).getBillingDate());
        assertTrue(periodOutputItemList.get(2).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 7, 31), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 10, 30), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 7, 31), periodOutputItemList.get(3).getBillingDate());
        assertTrue(periodOutputItemList.get(3).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 10, 31), periodOutputItemList.get(4).getStartDate());
        assertEquals(LocalDate.of(2016, 1, 30), periodOutputItemList.get(4).getEndDate());
        assertEquals(LocalDate.of(2015, 10, 31), periodOutputItemList.get(4).getBillingDate());
        assertTrue(periodOutputItemList.get(4).getIsFullMonth());

        assertEquals(LocalDate.of(2016, 1, 31), periodOutputItemList.get(5).getStartDate());
        assertEquals(LocalDate.of(2016, 4, 29), periodOutputItemList.get(5).getEndDate());
        assertEquals(LocalDate.of(2016, 1, 31), periodOutputItemList.get(5).getBillingDate());
        assertTrue(periodOutputItemList.get(5).getIsFullMonth());

    }

    @Test
    public void testMonthNotAlign4Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateMonthPeriodInputItem(LocalDate.of(2015, 1, 10), LocalDate.of(2016, 5, 14), "ARREARS", 3, 31);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(5, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 30), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 1, 31), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 4, 29), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 4, 30), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 7, 30), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 7, 30), periodOutputItemList.get(2).getBillingDate());
        assertTrue(periodOutputItemList.get(2).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 7, 31), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 10, 30), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 10, 30), periodOutputItemList.get(3).getBillingDate());
        assertTrue(periodOutputItemList.get(3).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 10, 31), periodOutputItemList.get(4).getStartDate());
        assertEquals(LocalDate.of(2016, 1, 30), periodOutputItemList.get(4).getEndDate());
        assertEquals(LocalDate.of(2016, 1, 30), periodOutputItemList.get(4).getBillingDate());
        assertTrue(periodOutputItemList.get(4).getIsFullMonth());

    }

    @Test
    public void testYearRegular() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateYearPeriodInputItem(LocalDate.of(2015, 1, 1), LocalDate.of(2017, 12, 31), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(2, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 12, 31), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 1, 1), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2016, 1, 1), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2016, 12, 31), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2016, 1, 1), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());

    }

    @Test
    public void testYearPartial() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateYearPeriodInputItem(LocalDate.of(2014, 10, 3), LocalDate.of(2016, 8, 31), "ADVANCE", 1, 37);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(2, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 2, 6), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2016, 2, 5), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 6), periodOutputItemList.get(0).getBillingDate());
        assertTrue(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2016, 2, 6), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2016, 8, 31), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2016, 2, 6), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());

    }

    @Test
    public void testYearPartialArrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateYearPeriodInputItem(LocalDate.of(2014, 10, 3), LocalDate.of(2016, 8, 31), "ARREARS", 1, 37);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(2, periodOutputItemList.size());

        assertEquals(LocalDate.of(2014, 10, 3), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 2, 5), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 2, 5), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 2, 6), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2016, 2, 5), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2016, 2, 5), periodOutputItemList.get(1).getBillingDate());
        assertTrue(periodOutputItemList.get(1).getIsFullMonth());
    }

    @Test
    public void testWeekRegular() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateWeekPeriodInputItem(LocalDate.of(2015, 8, 31), LocalDate.of(2015, 10, 20), "ADVANCE", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(4, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 7), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 13), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 7), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 14), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 14), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 28), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 10, 4), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 28), periodOutputItemList.get(3).getBillingDate());
        assertFalse(periodOutputItemList.get(3).getIsFullMonth());

    }

    @Test
    public void testWeekRegularArrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateWeekPeriodInputItem(LocalDate.of(2015, 8, 31), LocalDate.of(2015, 10, 20), "ARREARS", 1, 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 7), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 13), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 13), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 14), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 21), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 27), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 27), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());

    }

    @Test
    public void testWeekPartial() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateWeekPeriodInputItem(LocalDate.of(2015, 9, 10), LocalDate.of(2015, 10, 1), "ADVANCE", 1, 3);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(4, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 10), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 10), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 16), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 22), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 16), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 30), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 10, 1), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 30), periodOutputItemList.get(3).getBillingDate());
        assertFalse(periodOutputItemList.get(3).getIsFullMonth());

    }

    @Test
    public void testWeekPartialArrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateWeekPeriodInputItem(LocalDate.of(2015, 9, 3), LocalDate.of(2015, 10, 2), "ARREARS", 1, 3);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(4, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 3), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 8), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 8), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 9), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 23), periodOutputItemList.get(3).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 29), periodOutputItemList.get(3).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 29), periodOutputItemList.get(3).getBillingDate());
        assertFalse(periodOutputItemList.get(3).getIsFullMonth());

    }

    @Test
    public void testDayRegular() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateDayPeriodInputItem(LocalDate.of(2015, 9, 15), LocalDate.of(2015, 9, 20), "ADVANCE", 1);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(6, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(5).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(5).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 20), periodOutputItemList.get(5).getBillingDate());
        assertFalse(periodOutputItemList.get(5).getIsFullMonth());
    }

    @Test
    public void testDayRegular1() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateDayPeriodInputItem(LocalDate.of(2015, 9, 15), LocalDate.of(2015, 10, 28), "ADVANCE", 10);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(2, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 24), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 25), periodOutputItemList.get(1).getStartDate());
        assertEquals(LocalDate.of(2015, 10, 4), periodOutputItemList.get(1).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 25), periodOutputItemList.get(1).getBillingDate());
        assertFalse(periodOutputItemList.get(1).getIsFullMonth());
    }

    @Test
    public void testDayRegular1Arrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateDayPeriodInputItem(LocalDate.of(2015, 9, 15), LocalDate.of(2015, 10, 28), "ARREARS", 10);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(1, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 24), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 24), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());
    }

    @Test
    public void testDayPartial() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateDayPeriodInputItem(LocalDate.of(2015, 9, 15), LocalDate.of(2015, 9, 28), "ADVANCE", 5);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 19), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 25), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 28), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 25), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());
    }

    @Test
    public void testDayPartialArrears() {
        PeriodInputItem inputItem;
        List<PeriodOutputItem> periodOutputItemList;

        inputItem = generateDayPeriodInputItem(LocalDate.of(2015, 9, 15), LocalDate.of(2015, 9, 28), "ARREARS", 5);
        periodOutputItemList = multiPeriodProcessor.generatePeriods(inputItem);
        assertEquals(3, periodOutputItemList.size());

        assertEquals(LocalDate.of(2015, 9, 15), periodOutputItemList.get(0).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 19), periodOutputItemList.get(0).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 19), periodOutputItemList.get(0).getBillingDate());
        assertFalse(periodOutputItemList.get(0).getIsFullMonth());

        assertEquals(LocalDate.of(2015, 9, 25), periodOutputItemList.get(2).getStartDate());
        assertEquals(LocalDate.of(2015, 9, 28), periodOutputItemList.get(2).getEndDate());
        assertEquals(LocalDate.of(2015, 9, 28), periodOutputItemList.get(2).getBillingDate());
        assertFalse(periodOutputItemList.get(2).getIsFullMonth());
    }


    public PeriodInputItem generateMonthPeriodInputItem(LocalDate chargeStartDate, LocalDate chargeEndDate, String timing, Integer periodLength, Integer billingDay) {
        MultiData multiData = new MultiData();
        multiData.setTiming(timing);
        multiData.setPeriodLengthUnit("MONTH");
        multiData.setPeriodLength(periodLength);
        multiData.setBillingDay(billingDay);
        String multiDataString = null;
        try {
            multiDataString = JsonUtil.entityToJson(multiData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PeriodInputItem item = new PeriodInputItem();
        item.setCalFromDate(LocalDate.of(2015, 1, 1));
        item.setCalToDate(LocalDate.of(2016, 2, 29));
        item.setStartDate(chargeStartDate);
        item.setEndDate(chargeEndDate);
        item.setBillingType("MULTI_PERIOD");
        item.setBillingData(multiDataString);

        return item;
    }

    public PeriodInputItem generateYearPeriodInputItem(LocalDate chargeStartDate, LocalDate chargeEndDate, String timing, Integer periodLength, Integer billingDay) {
        MultiData multiData = new MultiData();
        multiData.setTiming(timing);
        multiData.setPeriodLengthUnit("YEAR");
        multiData.setPeriodLength(periodLength);
        multiData.setBillingDay(billingDay);
        String multiDataString = null;
        try {
            multiDataString = JsonUtil.entityToJson(multiData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PeriodInputItem item = new PeriodInputItem();
        item.setCalFromDate(LocalDate.of(2015, 1, 1));
        item.setCalToDate(LocalDate.of(2016, 2, 29));
        item.setStartDate(chargeStartDate);
        item.setEndDate(chargeEndDate);
        item.setBillingType("MULTI_PERIOD");
        item.setBillingData(multiDataString);

        return item;
    }

    public PeriodInputItem generateWeekPeriodInputItem(LocalDate chargeStartDate, LocalDate chargeEndDate, String timing, Integer periodLength, Integer billingDay) {
        MultiData multiData = new MultiData();
        multiData.setTiming(timing);
        multiData.setPeriodLengthUnit("WEEK");
        multiData.setPeriodLength(periodLength);
        multiData.setBillingDay(billingDay);
        String multiDataString = null;
        try {
            multiDataString = JsonUtil.entityToJson(multiData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PeriodInputItem item = new PeriodInputItem();
        item.setCalFromDate(LocalDate.of(2015, 9, 7));
        item.setCalToDate(LocalDate.of(2015, 10, 1));
        item.setStartDate(chargeStartDate);
        item.setEndDate(chargeEndDate);
        item.setBillingType("MULTI_PERIOD");
        item.setBillingData(multiDataString);

        return item;
    }

    public PeriodInputItem generateDayPeriodInputItem(LocalDate chargeStartDate, LocalDate chargeEndDate, String timing, Integer periodLength) {
        MultiData multiData = new MultiData();
        multiData.setTiming(timing);
        multiData.setPeriodLengthUnit("DAY");
        multiData.setPeriodLength(periodLength);
        String multiDataString = null;
        try {
            multiDataString = JsonUtil.entityToJson(multiData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PeriodInputItem item = new PeriodInputItem();
        item.setCalFromDate(LocalDate.of(2015, 9, 7));
        item.setCalToDate(LocalDate.of(2015, 10, 1));
        item.setStartDate(chargeStartDate);
        item.setEndDate(chargeEndDate);
        item.setBillingType("MULTI_PERIOD");
        item.setBillingData(multiDataString);

        return item;
    }

}