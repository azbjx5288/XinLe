package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

import java.util.Date;

/**
 * 资金明细
 * Created by Alashi on 2016/1/27.
 */
@RequestConfig(api = "service?packet=Fund&action=getTransactionList")
public class OrderListCommand {
    private Integer type_id=null;
    private Date begin_time;
    private Date end_time;
    private int page=1;
    private int pagesize=30;
    private String token;

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    /**0：全部
     1：投注，含撤单返款
     2：派奖，含撤销派奖
     3：充值
     4：提现， 含取消取现
     */

    public void setToken(String token) {
        this.token = token;
    }
}
