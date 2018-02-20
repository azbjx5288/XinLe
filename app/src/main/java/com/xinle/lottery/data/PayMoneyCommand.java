package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 提交订单
 * Created by ACE-PC on 2016/2/4. ?packet=Game&action=bet
 */
@RequestConfig(api = "service", method = Request.Method.POST, response = String.class)
public class PayMoneyCommand {
    @SerializedName("packet")
    private String _packet = "Game";
    @SerializedName("action")
    private String _action = "bet";
    @SerializedName("betdata")
    private String _betdata;
    @SerializedName("token")
    private String _token;                //1453787628864ie69o9oyi3mgr77uvppedx03o4yx5qxk	订单唯一凭据
    private String sign;

    public void set_packet(String _packet) {
        this._packet = _packet;
    }

    public void set_action(String _action) {
        this._action = _action;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void set_betdata(String _betdata) {
        this._betdata = _betdata;
    }

    public void setSign(String _sign) {
        this.sign = _sign;
    }
}
