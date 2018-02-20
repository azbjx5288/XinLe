package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 取消订单
 * Created by Alashi on 2016/2/5.
 */
@RequestConfig(api = "service?packet=Game&action=dropProject", method = Request.Method.POST)
public class CancelPackageCommand {
    @SerializedName("id")
    private String _Id;
    @SerializedName("token")
    private String _Token;
    private String sign;

    public void set_Id(String _Id) {
        this._Id = _Id;
    }

    public void set_Token(String _Token) {
        this._Token = _Token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
