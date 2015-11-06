package com.itranswarp.recurring.price.processor.recurring.flatfee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itranswarp.recurring.common.util.JsonUtil;
import com.itranswarp.recurring.price.PriceInputItem;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by changsure on 15/9/8.
 */
public class RecurringFlatFeeProcessorTest extends TestCase {

    RecurringFlatFeeProcessor flatFeeProcessor;

    @Before
    public void setUp() {
        flatFeeProcessor = new RecurringFlatFeeProcessor();
    }

    @Test
    public void testCalculateFullMonth1() {

        PriceInputItem input = createMonthInputItem();
        Double total = 0.0;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2015, 6, 1));
        input.setEndDate(LocalDate.of(2015, 6, 30));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2015, 2, 1));
        input.setEndDate(LocalDate.of(2015, 2, 28));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

    }

    public void testCalculateFullMonth2() {

        PriceInputItem input = createMonthInputItem();
        Double total = 0.0;

        input.setStartDate(LocalDate.of(2015, 1, 15));
        input.setEndDate(LocalDate.of(2015, 2, 14));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2015, 6, 30));
        input.setEndDate(LocalDate.of(2015, 7, 29));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2015, 2, 28));
        input.setEndDate(LocalDate.of(2015, 3, 27));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);
    }

    public void testCalculateFullMonth3() {

        PriceInputItem input = createMonthInputItem();
        input.setIsFullMonth(true);
        Double total = 0.0;

        input.setStartDate(LocalDate.of(2014, 2, 28));
        input.setEndDate(LocalDate.of(2014, 3, 30));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2014, 1, 31));
        input.setEndDate(LocalDate.of(2014, 2, 27));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2014, 1, 29));
        input.setEndDate(LocalDate.of(2014, 2, 27));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2014, 12, 30));
        input.setEndDate(LocalDate.of(2015, 1, 30));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2014, 1, 30));
        input.setEndDate(LocalDate.of(2018, 2, 27));
        total = flatFeeProcessor.calculate(input);
        assertEquals(4900.0, total);
    }

    @Test
    public void testCalculatePartialMonth() {
        PriceInputItem input = createMonthInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 1));
        total = flatFeeProcessor.calculate(input);
        assertEquals(3.23, total);

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 30));
        total = flatFeeProcessor.calculate(input);
        assertEquals(96.77, total);

        input.setStartDate(LocalDate.of(2014, 2, 28));
        input.setEndDate(LocalDate.of(2014, 3, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(112.90, total);
    }

    @Test
    public void testCalculateFullWeek() {
        PriceInputItem input = createWeekInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 7));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.00, total);

        input.setStartDate(LocalDate.of(2015, 1, 2));
        input.setEndDate(LocalDate.of(2015, 1, 8));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.00, total);

        input.setStartDate(LocalDate.of(2014, 2, 28));
        input.setEndDate(LocalDate.of(2014, 3, 6));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.00, total);
    }

    @Test
    public void testCalculatePartialWeek() {
        PriceInputItem input = createWeekInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 1));
        total = flatFeeProcessor.calculate(input);
        assertEquals(14.29, total);

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 2));
        total = flatFeeProcessor.calculate(input);
        assertEquals(28.57, total);

        input.setStartDate(LocalDate.of(2014, 2, 28));
        input.setEndDate(LocalDate.of(2014, 4, 5));
        total = flatFeeProcessor.calculate(input);
        assertEquals(528.57, total);
    }

    @Test
    public void testCalculateMultiDays() {
        PriceInputItem input = createDayInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 1));
        total = flatFeeProcessor.calculate(input);
        assertEquals(1.00, total);

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 2));
        total = flatFeeProcessor.calculate(input);
        assertEquals(2.00, total);

        input.setStartDate(LocalDate.of(2014, 1, 1));
        input.setEndDate(LocalDate.of(2014, 12, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(365.00, total);

        input.setStartDate(LocalDate.of(2014, 1, 1));
        input.setEndDate(LocalDate.of(2018, 12, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(1826.0, total);
    }

    @Test
    public void testCalculateFullYear() {
        PriceInputItem input = createYearInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 12, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(100.0, total);

        input.setStartDate(LocalDate.of(2014, 1, 1));
        input.setEndDate(LocalDate.of(2018, 12, 31));
        total = flatFeeProcessor.calculate(input);
        assertEquals(500.0, total);
    }

    @Test
    public void testCalculatePartialYear() {
        PriceInputItem input = createYearInputItem();
        Double total;

        input.setStartDate(LocalDate.of(2015, 1, 1));
        input.setEndDate(LocalDate.of(2015, 1, 1));
        total = flatFeeProcessor.calculate(input);
        assertEquals(0.27, total);

        input.setStartDate(LocalDate.of(2014, 1, 1));
        input.setEndDate(LocalDate.of(2014, 1, 2));
        total = flatFeeProcessor.calculate(input);
        assertEquals(0.55, total);

        input.setStartDate(LocalDate.of(2014, 1, 1));
        input.setEndDate(LocalDate.of(2018, 12, 30));
        total = flatFeeProcessor.calculate(input);
        assertEquals(499.89, total);
    }

    private PriceInputItem createMonthInputItem() {
        RecurringFlatFeeData data = new RecurringFlatFeeData();
        data.setPrice(100.0);
        data.setPriceBase("MONTH");
        data.setProrationType("");
        data.setDayOfMonth(31);

        String priceData = null;
        try {
            priceData = JsonUtil.entityToJson(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PriceInputItem inputItem = new PriceInputItem();
        inputItem.setPriceType("FLAT_FEE");
        inputItem.setPriceData(priceData);
        inputItem.setIsFullMonth(false);

        return inputItem;
    }

    private PriceInputItem createWeekInputItem() {
        RecurringFlatFeeData data = new RecurringFlatFeeData();
        data.setPrice(100.0);
        data.setPriceBase("WEEK");
        data.setProrationType("");

        String priceData = null;
        try {
            priceData = JsonUtil.entityToJson(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PriceInputItem inputItem = new PriceInputItem();
        inputItem.setPriceType("FLAT_FEE");
        inputItem.setPriceData(priceData);

        return inputItem;
    }

    private PriceInputItem createDayInputItem() {
        RecurringFlatFeeData data = new RecurringFlatFeeData();
        data.setPrice(1.0);
        data.setPriceBase("DAY");
        data.setProrationType("");

        String priceData = null;
        try {
            priceData = JsonUtil.entityToJson(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PriceInputItem inputItem = new PriceInputItem();
        inputItem.setPriceType("FLAT_FEE");
        inputItem.setPriceData(priceData);

        return inputItem;
    }

    private PriceInputItem createYearInputItem() {
        RecurringFlatFeeData data = new RecurringFlatFeeData();
        data.setPrice(100.00);
        data.setPriceBase("YEAR");
        data.setProrationType("");
        data.setDayOfYear(365);
        data.setDayOfMonth(31);

        String priceData = null;
        try {
            priceData = JsonUtil.entityToJson(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PriceInputItem inputItem = new PriceInputItem();
        inputItem.setPriceType("FLAT_FEE");
        inputItem.setPriceData(priceData);

        return inputItem;
    }


}