package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 追号订单列表
 * Created by Alashi on 2016/1/21.
 */
@RequestConfig(api = "service?packet=Game&action=getTraceList")
public class TraceListCommand {
    private int lottery_id;
    private int page;
    private int pagesize = 20;
    private String token;

    public void setLottery_id(int lottery_id) {
        this.lottery_id = lottery_id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
