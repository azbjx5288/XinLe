package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ACE-PC on 2016/5/4.
 */
public class LhcRebateOptions {
    /**
     * method_id : 583
     * method_name : 特码直选
     * options : [{"rebate":"0.000","prize":"90"}]
     */

    @SerializedName("method_id")
    private int methodId;
    @SerializedName("method_name")
    private String methodName;
    @SerializedName("options")
    private List<Options> options;

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public int getMethodId() {
        return methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Options> getOptions() {
        return options;
    }
}
