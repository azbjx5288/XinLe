package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by Ace.Dong on 2017/11/23.
 */
@RequestConfig(api = "service?packet=Game&action=getThirdLotteryLink")
public class ThirdLotteryLinkCommand {
    private String lottery;
    private String token;

    public void setLottery(String lottery) {
        this.lottery = lottery;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
