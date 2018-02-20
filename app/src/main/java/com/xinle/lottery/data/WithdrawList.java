package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ACE-PC on 2016/3/17.
 */
public class WithdrawList {

    /**
     * startDate : 2016-03-18
     * endDate : 2016-03-18
     * withdraws : []
     * totalAmount : 0
     * pageAmount : 0
     * errors : [{"err":101,"str":"账号、用户名不符"},{"err":102,"str":"银行维护"},{"err":103,"str":"流水不足充值的30%"},{"err":104,"str":"24小时之内更改了绑定银行卡"},{"err":105,"str":"24小时之内修改了安全信息"},{"err":106,"str":"投注流水异常"},{"err":107,"str":"提款系统维护"}]
     */

    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("totalAmount")
    private int totalAmount;
    @SerializedName("pageAmount")
    private int pageAmount;
    @SerializedName("count")
    private int count;
    @SerializedName("curPage")
    private int curPage;
    @SerializedName("withdraws")
    private List<Withdraws> withdraws;
    /**
     * err : 101
     * str : 账号、用户名不符
     */

    @SerializedName("errors")
    private List<ErrorsEntity> errors;

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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public List<Withdraws> getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(List<Withdraws> withdraws) {
        this.withdraws = withdraws;
    }

    public List<ErrorsEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsEntity> errors) {
        this.errors = errors;
    }
}
