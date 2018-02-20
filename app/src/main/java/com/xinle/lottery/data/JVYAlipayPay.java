package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/1/31.
 */

public class JVYAlipayPay {

    /**
     * version : 1
     * agent_id : yaofacai01
     * agent_bill_id : 19958872985890585392cf7
     * agent_bill_time : 20170131172643
     * pay_type : 50
     * pay_amt : 99.00
     * notify_url : http://www.jpg888.net/dnotify/hyzfb
     * user_ip : 180.232.127.100
     * sign : b81c7d2b96284f56adf16994d287d7df
     * is_phone : 1
     * goods_name : Vitrual66818
     * return_url :
     * goods_note :
     */

    private String version;
    private String agent_id;
    private String agent_bill_id;
    private String agent_bill_time;
    private String pay_type;
    private String pay_amt;
    private String notify_url;
    private String user_ip;
    private String sign;
    private String is_phone;
    private String goods_name;
    private String return_url;
    private String goods_note;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_bill_id() {
        return agent_bill_id;
    }

    public void setAgent_bill_id(String agent_bill_id) {
        this.agent_bill_id = agent_bill_id;
    }

    public String getAgent_bill_time() {
        return agent_bill_time;
    }

    public void setAgent_bill_time(String agent_bill_time) {
        this.agent_bill_time = agent_bill_time;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(String pay_amt) {
        this.pay_amt = pay_amt;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getIs_phone() {
        return is_phone;
    }

    public void setIs_phone(String is_phone) {
        this.is_phone = is_phone;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getGoods_note() {
        return goods_note;
    }

    public void setGoods_note(String goods_note) {
        this.goods_note = goods_note;
    }
}
