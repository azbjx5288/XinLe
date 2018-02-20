package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 申请提现
 * Created by Alashi on 2016/3/17.
 */
@RequestConfig(api = "service?packet=Fund&action=withdraw", method = Request.Method.POST)
public class WithdrawCommand {

    @SerializedName("bankcard_id")
    private String _bankcardId;
    @SerializedName("amount")
    private String _amount;
    @SerializedName("fund_password")
    private String _fundPassword;
    @SerializedName("token")
    private String _token;
    private String sign;

    public void set_bankcardId(String _bankcardId) {
        this._bankcardId = _bankcardId;
    }

    public void set_amount(String _amount) {
        this._amount = _amount;
    }

    public void set_fundPassword(String _fundPassword) {
        this._fundPassword = _fundPassword;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
