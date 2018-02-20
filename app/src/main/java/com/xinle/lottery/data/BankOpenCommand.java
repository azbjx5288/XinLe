package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/5/6.
 */
@RequestConfig(api = "service?packet=User&action=getBankList", method = Request.Method.GET)
public class BankOpenCommand {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
