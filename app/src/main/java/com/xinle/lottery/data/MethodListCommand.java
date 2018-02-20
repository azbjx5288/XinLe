package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * 某彩种玩法信息查询
 * Created by ACE-PC on 2016/1/21.
 */
@RequestConfig(api = "service?packet=Game&action=getWaySettings")
public class MethodListCommand {
    /**Int	不为空	lotteryID，空表示查所有彩种*/
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
