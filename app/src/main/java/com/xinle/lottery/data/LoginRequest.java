package com.xinle.lottery.data;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.net.RestRequest;
import com.xinle.lottery.base.net.RestResponse;
import com.xinle.lottery.user.UserCentre;

/**
 * Created by Alashi on 2016/1/6.
 */
public class LoginRequest extends RestRequest {
    public LoginRequest(Context context) {
        super(context);
    }

    @Override
    protected void onBackgroundResult(NetworkResponse networkResponse, RestResponse response) {
        UserCentre userCentre = XinLeApp.getUserCentre();
        if (response.getErrno() != 0) {
            userCentre.saveSession(null);
            return;
        }
        userCentre.saveLoginInfo(((LoginCommand)command).getUsername(), ((LoginCommand)command).getPassword());
        userCentre.setUserInfo((UserInfo) response.getData());

        if (userCentre.getUserInfo().getToken() == null) {
            return;
        }
        userCentre.saveSession(userCentre.getUserInfo().getToken());
    }
}
