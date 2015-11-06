package com.itranswarp.recurring.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

@Entity
public class Charge extends BaseEntity {

    /**
     * Reference to ChargeData object:
     * <p>
     * Charge 1 - N ChargeData
     */

    @Column(length = ID_LENGTH, nullable = false)
    String chargeDataId;

    @Column(length = VARCHAR_100, nullable = false, updatable = true)
    String status;

    public String getChargeDataId() {
        return chargeDataId;
    }

    public void setChargeDataId(String chargeDataId) {
        this.chargeDataId = chargeDataId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class ChargeStatus{
        public static final String ACTIVE = "ACTIVE";
        public static final String TRASH = "TRASH";
    }

}

