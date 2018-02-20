package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ace.Dong on 2017/11/23.
 */

public class RequestUrl {

    /**
     * request_url : http://startgame.la/107/?merId=3&userName=appace&roomId=1&tabled=1&token=07abe8945529e70e4421931d7f12048a
     */

    @SerializedName("request_url")
    private String requestUrl;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
