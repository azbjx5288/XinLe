package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * 开奖信息获取
 * Created by ACE-PC on 2016/1/22.
 */

@RequestConfig(api = "service?packet=Game&action=getCurrentIssue")
public class IssueInfoCommand {

    private int lottery_id;

    private String token;

    public int getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(int lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

