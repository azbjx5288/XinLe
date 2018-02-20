package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 修改密码（登录密码或资金密码）
 * Created by Alashi on 2016/5/2.
 */
@RequestConfig(api = "service?packet=User&action=changeLoginPwd",method = Request.Method.POST)
public class ChangePasswordCommand {
    @SerializedName("current_password")
    private String _currentPassword;
    @SerializedName("new_password")
    private String _newPassword;
    @SerializedName("token")
    private String _token;
    private String sign;

    public void set_currentPassword(String _currentPassword) {
        this._currentPassword = _currentPassword;
    }

    public void set_newPassword(String _newPassword) {
        this._newPassword = _newPassword;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
