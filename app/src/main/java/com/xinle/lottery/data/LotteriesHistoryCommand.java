package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/1/26.
 */
@RequestConfig(api = "service?packet=Game&action=getWnNumbersOfAllGames")
public class LotteriesHistoryCommand {
    private int count=5;
    private String token;

    public void setCount(int count) {
        this.count = count;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
