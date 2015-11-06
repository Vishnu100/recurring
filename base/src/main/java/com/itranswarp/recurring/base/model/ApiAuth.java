package com.itranswarp.recurring.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

/**
 * ApiAuth stores a key-secret pair that associate to a user.
 * 
 * @author michael
 */
@Entity
public class ApiAuth extends BaseEntity {

	@Column(nullable = false)
	boolean disabled;

	@Column(length = ID_LENGTH, nullable = false, updatable = false)
	String userId;

	@Column(length = VARCHAR_100, nullable = false)
	String apiKey;

	@Column(length = VARCHAR_100, nullable = false)
	String apiSecret;

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

}
