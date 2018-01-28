package com.gadiness.kimaru.ussdhelper.mzigos;

/**
 * Created by kimaru on 10/6/17.
 */

public class PhoneQueue {
    int id, branchId, queueId, phoneId, status;
    String phoneNumber, errorMessage, country, assignedTo, branchName;
    boolean sent, deleted, synced;
    int color = -1;

    public PhoneQueue() {}

    public PhoneQueue(int id, int branchId, String phoneNumber, int status,
                      String errorMessage, String country, boolean sent, boolean deleted,
                      boolean synced, int queueId, String assignedTo, String branchName, int phoneId) {
        this.id = id;
        this.branchId = branchId;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.errorMessage = errorMessage;
        this.country = country;
        this.sent = sent;
        this.deleted = deleted;
        this.synced = synced;
        this.queueId = queueId;
        this.assignedTo = assignedTo;
        this.branchName= branchName;
        this.phoneId = phoneId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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
