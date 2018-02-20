package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * 开奖号码信息
 * Created by ACE-PC on 2016/1/27.
 */
public class LastIssueInfo {

    /**
     * issue_id : 5059604
     * issue : 20160122-0831
     * code : 90127
     */

    @SerializedName("issueInfo")
    private LastIssueInfoEntity issueInfo;
    /**
     * issueInfo : {"issue_id":"5059604","issue":"20160122-0831","code":"90127"}
     * serverTime : 2016/01/22 14:52:28
     */

    @SerializedName("serverTime")
    private String serverTime;

    public void setIssueInfo(LastIssueInfoEntity issueInfo) {
        this.issueInfo = issueInfo;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public LastIssueInfoEntity getIssueInfo() {
        return issueInfo;
    }

    public String getServerTime() {
        return serverTime;
    }
}