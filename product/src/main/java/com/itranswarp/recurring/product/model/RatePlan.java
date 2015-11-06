package com.itranswarp.recurring.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

@Entity
public class RatePlan extends BaseEntity {

    /**
     * Reference to Product object.
     * 
     * Product 1 - N RatePlan
     */
    @Column(length=ID_LENGTH, nullable=false)
    String productId;

}
