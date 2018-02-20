package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/12/29.
 */
@RequestConfig(api = "service?packet=Fund&action=deposit")
public class ChannelInfoCommand {
    private int platform_id;
    private double amount;
    private int bank_id;
    private String is_phone;
    private String token;

    public void setPlatform_id(int platform_id) {
        this.platform_id = platform_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public void setIs_phone(String is_phone) {
        this.is_phone = is_phone;
    }

    public void setToken(String token) {
        this.token = token;
    }
}