package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/5/4.
 */

@RequestConfig(api = "service?packet=Game&action=getAvailablePrizeGroups")
public class RegChildRebateCommand {
    private int lottery_id;
    private String token;

    public void setLottery_id(int lottery_id) {
        this.lottery_id = lottery_id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
