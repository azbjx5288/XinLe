package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * 追号订单详情
 * Created by Alashi on 2016/1/21.
 */
@RequestConfig(api = "service?packet=Game&action=getTraceDetail")
public class TraceDetailCommand {
    private int id;
    private String token;

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
