package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2017/2/3.
 */
@RequestConfig(api = "service?packet=Fund&action=depositQuery")
public class RechargeCallbackCommand {
    private String order_no;
    private String token;

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
