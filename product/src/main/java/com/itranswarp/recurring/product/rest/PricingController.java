package com.itranswarp.recurring.product.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itranswarp.recurring.product.pricing.FlatFeePricing;
import com.itranswarp.recurring.product.pricing.OveragePricing;
import com.itranswarp.recurring.product.pricing.PerUnitPricing;
import com.itranswarp.recurring.product.pricing.TieredWithOveragePricing;
import com.itranswarp.recurring.product.pricing.VolumePricing;
import com.itranswarp.recurring.product.pricing.info.UnitPriceInfo;

/**
 * Online calculate pricing.
 * 
 * @author michael
 */
@RestController
public class PricingController {

	@RequestMapping(value="/api/pricing/flatfee", method=RequestMethod.POST)
	public PricingResult calculateFlatFeePricing(@RequestBody FlatFeeModel model) {
		return resultOf(model.pricing.calculate(null));
	}

	@RequestMapping(value="/api/pricing/perUnit", method=RequestMethod.POST)
	public PricingResult calculatePerUnitPricing(@RequestBody PerUnitModel model) {
		return resultOf(model.pricing.calculate(model.priceInfo));
	}

	@RequestMapping(value="/api/pricing/overage", method=RequestMethod.POST)
	public PricingResult calculateOveragePricing(@RequestBody OverageModel model) {
		return resultOf(model.pricing.calculate(model.priceInfo));
	}

	@RequestMapping(value="/api/pricing/tieredWithOverage", method=RequestMethod.POST)
	public PricingResult calculateVolumePricing(@RequestBody TieredWithOverageModel model) {
		return resultOf(model.pricing.calculate(model.priceInfo));
	}

	@RequestMapping(value="/api/pricing/volume", method=RequestMethod.POST)
	public PricingResult calculateVolumePricing(@RequestBody VolumeModel model) {
		return resultOf(model.pricing.calculate(model.priceInfo));
	}

	PricingResult resultOf(double price) {
		return new PricingResult(price);
	}
}

class FlatFeeModel {
	FlatFeePricing pricing;
}

class OverageModel {
	OveragePricing pricing;
	UnitPriceInfo priceInfo;
}

class PerUnitModel {
	PerUnitPricing pricing;
	UnitPriceInfo priceInfo;
}

class TieredWithOverageModel {
	TieredWithOveragePricing pricing;
	UnitPriceInfo priceInfo;
}

class VolumeModel {
	VolumePricing pricing;
	UnitPriceInfo priceInfo;
}

class PricingResult {

	double price;

	public PricingResult(double price) {
		this.price = price;
	}
}
