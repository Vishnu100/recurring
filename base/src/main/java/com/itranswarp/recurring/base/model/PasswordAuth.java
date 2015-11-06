package com.itranswarp.recurring.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * Using password authenticate.
 * 
 * @author michael
 */
@Entity
public class PasswordAuth extends BaseEntity {

    @Column(length=ID_LENGTH, unique=true, nullable=false, updatable=false)
    String userId;

    /**
     * Store SHA-1 password generated as:
     * 
     * sha1(id + ":" + inputPassword))
     */
    @Column(length=VARCHAR_100, nullable=false)
    String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
