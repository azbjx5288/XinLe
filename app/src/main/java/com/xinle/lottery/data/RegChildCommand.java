package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

/**
 * Created by ACE-PC on 2016/5/4.
 */

@RequestConfig(api = "?c=user&a=regChild", response = String.class)
public class RegChildCommand {
    private String op;
    private int type;
    private String username;
    private String password;
    private String password2;
    private String normal_rebate;
    private String lhc_rebate;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getNormal_rebate() {
        return normal_rebate;
    }

    public void setNormal_rebate(String normal_rebate) {
        this.normal_rebate = normal_rebate;
    }

    public String getLhc_rebate() {
        return lhc_rebate;
    }

    public void setLhc_rebate(String lhc_rebate) {
        this.lhc_rebate = lhc_rebate;
    }
}
