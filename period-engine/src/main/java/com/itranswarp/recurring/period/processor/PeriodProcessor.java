package com.itranswarp.recurring.period.processor;

import com.itranswarp.recurring.period.dto.PeriodInputItem;
import com.itranswarp.recurring.period.dto.PeriodOutputItem;

import java.util.List;

/**
 * Created by changsure on 15/8/26.
 */
public interface PeriodProcessor {

    void checkData(String billingData);

    List<PeriodOutputItem> generatePeriods(PeriodInputItem inputItem);
}
