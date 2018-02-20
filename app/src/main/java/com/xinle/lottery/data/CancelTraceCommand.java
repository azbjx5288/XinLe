package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 追号撤单
 * Created by Alashi on 2016/2/8.
 */
@RequestConfig(api = "service?packet=Game&action=cancelTraceReserve", method = Request.Method.POST)
public class CancelTraceCommand {
    @SerializedName("trace_id")
    private String _traceId;
    @SerializedName("ids")
    private String _ids;
    @SerializedName("token")
    private String _token;
    private String sign;

    public void set_traceId(String _traceId) {
        this._traceId = _traceId;
    }

    public void set_ids(String _ids) {
        this._ids = _ids;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
