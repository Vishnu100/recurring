package com.itranswarp.recurring.price.processor.recurring.perunit;

import com.itranswarp.recurring.price.processor.recurring.RecurringData;

/**
 * Created by changsure on 15/9/17.
 */
public class RecurringPerUnitData extends RecurringData {

    Double unitPrice;

    String uom;

    Double quantity;

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public Double getPrice(){
        return this.getUnitPrice()*this.getQuantity();
    }
}
