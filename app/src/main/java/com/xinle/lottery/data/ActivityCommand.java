package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/8/3.
 */
@RequestConfig(api = "service?packet=Event&action=getValidEvent", method = Request.Method.GET)
public class ActivityCommand {
    @SerializedName("user_id")
    private int userdId;
    private String token;

    public void setUserdId(int userdId) {
        this.userdId = userdId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}