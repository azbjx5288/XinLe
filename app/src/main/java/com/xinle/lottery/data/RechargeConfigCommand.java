package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2016/6/24.
 * 1 网银2 微信3 支付宝4 银行卡
 */
@RequestConfig(api = "service?packet=Fund&action=getDepositPlatformList")
public class RechargeConfigCommand {
    @SerializedName("pament_type_id")
    private String pamentTypeId;
    private String token;

    public void setPamentTypeId(String pamentTypeId) {
        this.pamentTypeId = pamentTypeId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
