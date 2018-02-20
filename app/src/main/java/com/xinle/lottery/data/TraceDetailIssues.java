package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ACE-PC on 2016/6/1.
 */
public class TraceDetailIssues {
    @SerializedName("id")
    private int id;
    @SerializedName("issue")
    private String issue;
    @SerializedName("multiple")
    private int multiple;
    @SerializedName("amount")
    private double amount;
    @SerializedName("project_id")
    private int projectId;
    @SerializedName("status")
    private int status;
    @SerializedName("bought_at")
    private Date boughtAt;
    @SerializedName("canceled_at")
    private Date canceledAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getBoughtAt() {
        return boughtAt;
    }

    public void setBoughtAt(Date boughtAt) {
        this.boughtAt = boughtAt;
    }

    public Date getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
    }
}
