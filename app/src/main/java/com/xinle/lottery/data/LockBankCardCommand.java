package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/3/3.
 */
@RequestConfig(api = "service?packet=User&action=lockBankCard", method = Request.Method.POST)
public class LockBankCardCommand {
    @SerializedName("fund_pwd")
    private String fundPwd;
    @SerializedName("token")
    private String token;
    private String sign;

    public void setToken(String token) {
        this.token = token;
    }

    public void setFundPwd(String fundPwd) {
        this.fundPwd = fundPwd;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
