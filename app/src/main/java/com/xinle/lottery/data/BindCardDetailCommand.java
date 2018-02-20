package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/5/5.
 */
@RequestConfig(api = "service?packet=User&action=getBankCardList", method = Request.Method.GET)
public class BindCardDetailCommand {
    private String begin_date;
    private String end_date;
    private String token;

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
