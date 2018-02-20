package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * 手机客户端登出
 * Created by User on 2016/1/15.
 */
@RequestConfig(api = "service?packet=User&action=logout")
public class LogoutCommand {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }
}
