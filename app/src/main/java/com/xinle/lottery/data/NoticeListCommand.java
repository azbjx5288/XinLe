package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 公告列表
 * Created by Alashi on 2016/1/19.
 */
@RequestConfig(api = "service?packet=Notice&action=getNoticeList")
public class NoticeListCommand {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
