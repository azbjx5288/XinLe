package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/2/20.
 */
@RequestConfig(api = "service?packet=User&action=setFundPwd",method = Request.Method.POST)
public class SetFundPwdCommand {
    @SerializedName("fund_pwd")
    private String fundPwd;
    @SerializedName("confirm_fund_pwd")
    private String confirmFundPwd;
    private String token;
    private String sign;

    public void setFundPwd(String fundPwd) {
        this.fundPwd = fundPwd;
    }

    public void setConfirmFundPwd(String confirmFundPwd) {
        this.confirmFundPwd = confirmFundPwd;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
