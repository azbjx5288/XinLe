package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/3/17.
 */
@RequestConfig(api = "?c=user&a=withdrawList",method = Request.Method.GET)
public class WithdrawListCommand {
    private String startDate;
    private String endDate;
    private int curPage=1;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
}
