package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2017/3/3.
 */
@RequestConfig(api = "service?packet=User&action=deleteBankCard",method = Request.Method.POST)
public class DeleteCommand {
    @SerializedName("card_id")
    private int cardId;
    @SerializedName("checked_token")
    private String checkedToken;
    private String token;
    private String sign;

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setCheckedToken(String checkedToken) {
        this.checkedToken = checkedToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
