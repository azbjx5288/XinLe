package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 修改安全信息
 * Created by Alashi on 2016/5/2.
 */
@RequestConfig(api = "service?packet=User&action=getBankCardList", method = Request.Method.GET)
public class EditSecurityCommand {
    private String real_name;
    private String secpassword;
    private String secpassword2;
    private String email;
    private String token;
    public void setRealName(String realName) {
        this.real_name = realName;
    }

    public void setSecpassword(String secpassword) {
        this.secpassword = secpassword;
    }

    public void setSecpassword2(String secpassword2) {
        this.secpassword2 = secpassword2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
