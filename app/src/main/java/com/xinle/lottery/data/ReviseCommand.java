package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/3/2.
 */
@RequestConfig(api = "service?packet=User&action=checkBankCard",method = Request.Method.POST)
public class ReviseCommand {
    @SerializedName("card_id")
    private int cardId;
    @SerializedName("fund_pwd")
    private String fundPwd;
    @SerializedName("account_name")
    private String account_name;
    @SerializedName("account")
    private String account;
    private int type;
    private String token;
    private String sign;

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setFundPwd(String fundPwd) {
        this.fundPwd = fundPwd;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
