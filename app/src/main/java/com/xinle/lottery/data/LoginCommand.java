package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * 手机客户端登录验证
 * Created by Alashi on 2016/1/5.
 */
@RequestConfig(api = "service?packet=User&action=login", response = UserInfo.class, request = LoginRequest.class)
public class LoginCommand {
    /**String(10)	不可为空	用户名 （绑定手机号登录功能二期实现）*/
    private String username;
    /**String(32)	不可为空	用户密码，经过md5加密，长度为32的字符串。*/
    private String password;
    /** 终端 ID */
    private int terminal_id=2;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTerminal_id(int terminal_id) {
        this.terminal_id = terminal_id;
    }
}
