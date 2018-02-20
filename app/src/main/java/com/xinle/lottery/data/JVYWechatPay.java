package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/1/31.
 */

public class JVYWechatPay {


    /**
     * version : 1
     * agent_id : yaofacai01
     * agent_bill_id : 763182862589c230e2c36d
     * agent_bill_time : 20170209160638
     * pay_type : 30
     * pay_amt : 66.00
     * notify_url : http://www.jpg888.net/dnotify/hywx
     * user_ip : 116.93.121.58
     * sign : ae1e39a71f749ebe7c690f6f55b9ca26
     * goods_name : Vitrual47588
     * remark : huiyuanwx_query
     */

    @SerializedName("version")
    private int version;
    @SerializedName("agent_id")
    private String agentId;
    @SerializedName("agent_bill_id")
    private String agentBillId;
    @SerializedName("agent_bill_time")
    private String agentBillTime;
    @SerializedName("pay_type")
    private int payType;
    @SerializedName("pay_amt")
    private String payAmt;
    @SerializedName("notify_url")
    private String notifyUrl;
    @SerializedName("user_ip")
    private String userIp;
    @SerializedName("sign")
    private String sign;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("remark")
    private String remark;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentBillId() {
        return agentBillId;
    }

    public void setAgentBillId(String agentBillId) {
        this.agentBillId = agentBillId;
    }

    public String getAgentBillTime() {
        return agentBillTime;
    }

    public void setAgentBillTime(String agentBillTime) {
        this.agentBillTime = agentBillTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
