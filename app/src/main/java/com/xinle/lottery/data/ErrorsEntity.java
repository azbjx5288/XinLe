package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2016/3/18.
 */
public class ErrorsEntity {
    @SerializedName("err")
    private int err;
    @SerializedName("str")
    private String str;

    public void setErr(int err) {
        this.err = err;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getErr() {
        return err;
    }

    public String getStr() {
        return str;
    }
}
