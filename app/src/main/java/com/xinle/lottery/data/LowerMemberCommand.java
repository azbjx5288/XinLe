package com.xinle.lottery.data;

import com.xinle.lottery.base.net.RequestConfig;

import java.util.Date;

/**
 * Created by ACE-PC on 2016/5/3.
 */

@RequestConfig(api = "service?packet=User&action=getUpdatedUsers")
public class LowerMemberCommand {
    private Date date;
    private String token;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
