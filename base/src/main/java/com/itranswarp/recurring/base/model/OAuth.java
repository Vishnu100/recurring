package com.itranswarp.recurring.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Using OAuth2 authentication.
 * 
 * @author michael
 */
@Entity
public class OAuth extends BaseEntity {

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String userId;

    @Column(length=VARCHAR_100, nullable=false, updatable=false)
    String provider;

    @Column(length=VARCHAR_1000, nullable=false)
    String oauthId;

    @Column(length=VARCHAR_1000, nullable=false)
    String accessToken;

    @Column(length=VARCHAR_1000, nullable=false)
    String refreshToken;

    @Column(nullable=false)
    long expiresAt;

}
