package com.itranswarp.recurring.product.service;

import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.period.service.PeriodService;
import com.itranswarp.recurring.price.service.PriceService;
import com.itranswarp.recurring.product.model.Charge;
import com.itranswarp.recurring.product.model.ChargeData;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

@Named
public class ChargeService {

    @Inject
    Database database;

    @Inject
    PriceService priceService;

    @Inject
    PeriodService periodService;

    @Transactional
    public Charge createCharge(ChargeData chargeData) {
        validChargeData(chargeData);

        Charge charge = new Charge();
        charge.setChargeDataId("");
        charge.setStatus(Charge.ChargeStatus.ACTIVE);
        database.save(charge);

        chargeData.setChargeId(charge.getId());
        chargeData.setPreviousId("");
        chargeData.setStatus(ChargeData.ChargeDataStatus.ACTIVE);
        database.save(chargeData);

        charge.setChargeDataId(chargeData.getId());
        database.update(charge);

        return charge;
    }

    @Transactional
    public Charge updateCharge(Charge newCharge, ChargeData chargeData) {
        validChargeData(chargeData);

        Charge charge = database.fetch(Charge.class, newCharge.getId());
        ChargeData oldChargeData = database.fetch(ChargeData.class, charge.getChargeDataId());

        oldChargeData.setStatus(ChargeData.ChargeDataStatus.EXPIRED);
        database.update(oldChargeData);
        chargeData.setPreviousId(oldChargeData.getId());
        chargeData.setChargeId(newCharge.getId());
        chargeData.setStatus(ChargeData.ChargeDataStatus.ACTIVE);
        database.save(chargeData);

        //NO other properties need to copy from charge
        charge.setChargeDataId(chargeData.getId());
        database.update(charge);
        return charge;
    }

    @Transactional
    public Charge deleteCharge(String chargeId) {
        Charge charge = database.fetch(Charge.class, chargeId);

        charge.setStatus(Charge.ChargeStatus.TRASH);
        database.update(charge);

        return charge;
    }

    public Charge readCharge(String chargeId) {
        Charge charge = database.fetch(Charge.class, chargeId);
        return charge;
    }

    public PagedResults<Charge> readCharges(Integer pageIndex, Integer itemsPerPage) {
        PagedResults<Charge> chargeList = database.from(Charge.class).where("status=?", Charge.ChargeStatus.ACTIVE).list(pageIndex, itemsPerPage);
        return chargeList;
    }

    public ChargeData readChargeDataById(String chargeDataId) {
        ChargeData chargeData = database.fetch(ChargeData.class, chargeDataId);
        return chargeData;
    }

    public List<ChargeData> readExpiredChargeDataByChargeId(String chargeId) {
        return database.from(ChargeData.class).where("chargeId=?", chargeId).and("status=?", ChargeData.ChargeDataStatus.EXPIRED).list();
    }

    private void validChargeData(ChargeData chargeData) {
        periodService.checkBillingData(chargeData.getBillingType(), chargeData.getBillingData());
        priceService.checkPriceData(chargeData.getPriceType(), chargeData.getPriceData());
    }

}

