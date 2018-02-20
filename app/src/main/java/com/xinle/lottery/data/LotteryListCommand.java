package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 彩票种类信息查询
 * Created by Alashi on 2016/1/5.
 */
@RequestConfig(api = "service?packet=Game&action=getAllGames")
public class LotteryListCommand {
    @SerializedName("game_type")
    private String gameType;
    private String token;

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
