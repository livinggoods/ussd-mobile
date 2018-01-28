package com.gadiness.kimaru.ussdhelper.mzigos;

/**
 * Created by kimaru on 1/24/18.
 */


public class Queue {
    String branchName, name, status, country;
    Integer id, branchId;
    boolean deleted, completed, selected;
    Long dateAdded;
    int color = -1;

    public Queue() {}

    public Queue(String branchName, String name, String status, String country, Integer id,
                 Integer branchId, boolean deleted, boolean completed, Long dateAdded,
                 boolean selected) {
        this.branchName = branchName;
        this.name = name;
        this.status = status;
        this.country = country;
        this.id = id;
        this.branchId = branchId;
        this.deleted = deleted;
        this.completed = completed;
        this.dateAdded = dateAdded;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCountry() {
        return country;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
