package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 获取自己绑定卡的信息
 * Created by Alashi on 2016/3/17.
 */
@RequestConfig(api = "service?packet=User&action=getBankCardList", method = Request.Method.GET)
public class CardDetailCommand {
    private String token= XinLeApp.getUserCentre().getUserInfo().getToken();
    public void setToken(String token) {
        this.token = token;
    }
}
