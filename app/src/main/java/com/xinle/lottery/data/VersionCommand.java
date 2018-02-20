package com.xinle.lottery.data;

import com.android.volley.Request;
import com.xinle.lottery.base.net.RequestConfig;

/**
 * 获取最新版本号
 * Created by Alashi on 2016/3/1.
 */
@RequestConfig(api = "service?packet=Release&action=getLatestRelease", response = Version.class)
public class VersionCommand {
    private int terminal_id=2;

    public void setTerminal_id(int terminal_id) {
        this.terminal_id = terminal_id;
    }
}
