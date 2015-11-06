package com.itranswarp.recurring.db.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    protected static final int ID_LENGTH = 50;
    protected static final int ENUM = 50;
    protected static final int VARCHAR_100 = 100;
    protected static final int VARCHAR_1000 = 1000;
    protected static final String COL_TEXT = "mediumtext";

    @Id
    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String id;

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String tenantId;

    @Column(nullable=false, updatable=false)
    long createdAt;

    @Column(length=ID_LENGTH, nullable=false, updatable=false)
    String createdBy;

    @Column(nullable=false)
    long updatedAt;

    @Column(length=ID_LENGTH, nullable=false)
    String updatedBy;

    @Column(nullable=false)
    long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
