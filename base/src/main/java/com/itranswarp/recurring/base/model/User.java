package com.itranswarp.recurring.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.itranswarp.recurring.db.model.BaseEntity;

@Entity
public class User extends BaseEntity {

    @Column(length=VARCHAR_100, nullable=false)
    String email;

    @Column(length=VARCHAR_100, nullable=false)
    String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
