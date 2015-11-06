package com.itranswarp.recurring.product.rest.vo;

import com.itranswarp.recurring.product.model.Charge;
import com.itranswarp.recurring.product.model.ChargeData;

import java.util.Collections;
import java.util.List;

/**
 * Created by changsure on 15/8/12.
 */
public class ChargeVo {

    Charge charge;

    ChargeData chargeData;

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public ChargeData getChargeData() {
        return chargeData;
    }

    public void setChargeData(ChargeData chargeData) {
        this.chargeData = chargeData;
    }

    public ChargeVo build(Charge charge, ChargeData chargeData){
        this.charge = charge;
        this.chargeData = chargeData;
        return this;
    }
}
