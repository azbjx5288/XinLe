package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by Ace.Dong on 2017/10/5.
 */
@RequestConfig(api = "service?packet=Game&action=getSeries", method = Request.Method.GET)
public class SeriesCommand {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
