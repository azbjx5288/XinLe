package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ACE-PC on 2016/3/1.
 */
@RequestConfig(api = "service?packet=Game&action=getWnNumberList")
public class LotteryCodeCommand {
    private int lottery_id;
    private int count=20;
    private String token;

    public void setLottery_id(int lottery_id) {
        this.lottery_id = lottery_id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
