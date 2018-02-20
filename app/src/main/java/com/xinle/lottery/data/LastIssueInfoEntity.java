package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * 开奖号码信息
 * Created by ACE-PC on 2016/1/27.
 */
public class LastIssueInfoEntity {
    /**
     * issue_id : 4961608
     * issue : 20160122-0815
     * code :
     */
    @SerializedName("issue_id")
    private String issueid;
    private String issue;
    private String code;

    public String getIssueid() {
        return issueid;
    }

    public void setIssueid(String issueid) {
        this.issueid = issueid;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}