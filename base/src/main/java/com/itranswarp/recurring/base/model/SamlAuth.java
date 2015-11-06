package com.itranswarp.recurring.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Using SAML authentication.
 * 
 * @author michael
 */
@Entity
public class SamlAuth extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String userId;

}
