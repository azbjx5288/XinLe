package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2016/5/27.
 */
@RequestConfig(api = "service?packet=User&action=changeFundPwd",method = Request.Method.POST)
public class ChangeFundCommand {
    @SerializedName("current_password")
    private String _current_password;
    @SerializedName("new_password")
    private String _new_password;
    private String token;
    private String sign;

    public void set_current_password(String _current_password) {
        this._current_password = _current_password;
    }

    public void set_new_password(String _new_password) {
        this._new_password = _new_password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
