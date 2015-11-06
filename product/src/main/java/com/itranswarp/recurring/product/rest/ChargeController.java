package com.itranswarp.recurring.product.rest;

import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.db.PagedResults;
import com.itranswarp.recurring.product.model.Charge;
import com.itranswarp.recurring.product.model.ChargeData;
import com.itranswarp.recurring.product.rest.vo.ChargeVo;
import com.itranswarp.recurring.product.service.ChargeService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by changsure on 15/8/12.
 */
@RestController
public class ChargeController {

    @Inject
    ChargeService chargeService;

    @RequestMapping(value = "/api/product/charge", method = RequestMethod.POST)
    public ChargeVo createCharge(@RequestBody ChargeVo chargeVo) {

        if (chargeVo.getChargeData() == null) {
            throw new APIArgumentException("chargeData", "Charge Data can not be null!");
        }

        Charge charge = chargeService.createCharge(chargeVo.getChargeData());
        ChargeData chargeData = chargeService.readChargeDataById(charge.getChargeDataId());
        chargeVo.setCharge(charge);
        chargeVo.setChargeData(chargeData);

        return chargeVo;
    }

    @RequestMapping(value = "/api/product/charge/{chargeId}", method = RequestMethod.PUT)
    public ChargeVo updateCharge(@PathVariable(value = "chargeId") String chargeId, @RequestBody ChargeVo chargeVo) {

        if (chargeVo.getChargeData() == null) {
            throw new APIArgumentException("chargeData", "Charge Data can not be null!");
        }

        Charge charge = new Charge();
        charge.setId(chargeId);

        charge = chargeService.updateCharge(charge, chargeVo.getChargeData());
        ChargeData chargeData = chargeService.readChargeDataById(charge.getChargeDataId());
        chargeVo.setCharge(charge);
        chargeVo.setChargeData(chargeData);

        return chargeVo;
    }

    @RequestMapping(value = "/api/product/charge/{chargeId}", method = RequestMethod.GET)
    public ChargeVo readCharge(@PathVariable(value = "chargeId") String chargeId) {

        Charge charge = chargeService.readCharge(chargeId);
        ChargeData chargeData = chargeService.readChargeDataById(charge.getChargeDataId());
        ChargeVo chargeVo = new ChargeVo();
        chargeVo.setCharge(charge);
        chargeVo.setChargeData(chargeData);

        return chargeVo;
    }

    @RequestMapping(value = "/api/product/charges", method = RequestMethod.GET)
    public PagedResults<ChargeVo> readCharges(@RequestParam Integer pageIndex, @RequestParam Integer itemsPerPage) {

        PagedResults<Charge> chargePagedResults = chargeService.readCharges(pageIndex, itemsPerPage);

        PagedResults<ChargeVo> results = chargePagedResults.map(
                (Charge charge) -> {
                    ChargeData chargeData = chargeService.readChargeDataById(charge.getChargeDataId());
                    return new ChargeVo().build(charge, chargeData);
                }
        );

        return results;
    }

    @RequestMapping(value = "/api/product/charge/{chargeId}", method = RequestMethod.DELETE)
    public ChargeVo deleteCharge(@PathVariable(value = "chargeId") String chargeId) {

        Charge charge = chargeService.deleteCharge(chargeId);
        return new ChargeVo().build(charge, null);
    }

    @RequestMapping(value = "/api/product/charge_data/{chargeDataId}", method = RequestMethod.GET)
    public ChargeData readChargeData(@PathVariable(value = "chargeDataId") String chargeDataId) {

        ChargeData chargeData = chargeService.readChargeDataById(chargeDataId);
        return chargeData;
    }

}
