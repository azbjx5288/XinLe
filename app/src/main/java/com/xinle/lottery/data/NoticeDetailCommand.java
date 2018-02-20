package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 获取公告或banner的详情
 * Created by User on 2016/2/19.
 */
@RequestConfig(api = "service?packet=Notice&action=getNoticeDetail", method = Request.Method.GET,response = NoticeDetail.class)
public class NoticeDetailCommand {

    private String id;
    private String token;

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
