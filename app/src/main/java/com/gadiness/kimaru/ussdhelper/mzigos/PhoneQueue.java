package com.gadiness.kimaru.ussdhelper.mzigos;

/**
 * Created by kimaru on 10/6/17.
 */

public class PhoneQueue {
    int id, branchId;
    String phoneNumber, status, errorMessage, country;
    boolean sent, deleted, synced;

    public PhoneQueue() {}

    public PhoneQueue(int id, int branchId, String phoneNumber, String status,
                      String errorMessage, String country, boolean sent, boolean deleted,
                      boolean synced) {
        this.id = id;
        this.branchId = branchId;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.errorMessage = errorMessage;
        this.country = country;
        this.sent = sent;
        this.deleted = deleted;
        this.synced = synced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
