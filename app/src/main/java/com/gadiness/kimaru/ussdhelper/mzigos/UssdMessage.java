package com.gadiness.kimaru.ussdhelper.mzigos;

/**
 * Created by kimaru on 10/6/17.
 */

public class UssdMessage {
    int id, messageTypeId, branchId, phoneId, queueId;
    String phoneNumber, message, country;
    double bundleBalance;
    Long expiryDateTime, dateAdded;
    boolean active, deleted, synced;

    public UssdMessage(){}

    public UssdMessage(int id, int messageTypeId, String phoneNumber, String message,
                       int branchId, int phoneId, String country, double bundleBalance,
                       Long expiryDateTime, Long dateAdded, boolean active, boolean deleted,
                       boolean synced, int queueId) {
        this.id = id;
        this.messageTypeId = messageTypeId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.branchId = branchId;
        this.phoneId = phoneId;
        this.country = country;
        this.bundleBalance = bundleBalance;
        this.expiryDateTime = expiryDateTime;
        this.dateAdded = dateAdded;
        this.active = active;
        this.deleted = deleted;
        this.synced = synced;
        this.queueId = queueId;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getBundleBalance() {
        return bundleBalance;
    }

    public void setBundleBalance(double bundleBalance) {
        this.bundleBalance = bundleBalance;
    }

    public Long getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(Long expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
