package com.xinle.lottery.data;

import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 当前用户所有信息查询
 * Created by Alashi on 2016/1/28.
 */
@RequestConfig(api = "service?packet=User&action=getCurrentUserInfo", response = UserInfo.class)
public class UserInfoCommand {
    private String token= XinLeApp.getUserCentre().getSession();
    public void setToken(String token) {
        this.token = token;
    }
}
