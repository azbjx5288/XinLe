package com.xinle.lottery.user;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.xinle.lottery.app.XinLeApp;
import com.xinle.lottery.base.Preferences;
import com.xinle.lottery.base.net.GsonHelper;
import com.xinle.lottery.data.LoginCommand;
import com.xinle.lottery.data.Lottery;
import com.xinle.lottery.data.Series;
import com.xinle.lottery.data.UserInfo;
import com.xinle.lottery.material.ShoppingCart;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alashi on 2016/1/5.
 */
public class UserCentre {
    private static final String TAG = UserCentre.class.getSimpleName();
    private static final String KEY_LOGIN_NAME = "user_login_name";
    private static final String KEY_LOGIN_PASSWORD = "user_login_password";
    private static final String KEY_SESSION = "user_session";
    private static final String KEY_INFO = "user_info";
    private static final String KEY_LOTTERY_MAP = "user_lottery_map";
    private static final String KEY_SERIES_MAP = "user_series_map";
    private static final String KEY_LOTTERY_LUCREMODE = "user_lottery_lucremode";
    private static final String KEY_LOTTERY_BONUSMODE = "user_lottery_bonusmode_";
    private static final String KEY_LOTTERY_PRIZEGROUP = "user_lottery_prizegroup";
    private static final String KEY_LOTTERY_RECHARGE_MODE="user_lottery_recharge_mode";

    private Context context;
    private String session;
    private String serverBase;
    private String userID;
    private UserInfo userInfo;
    private SparseArray<Series> seriesMap = new SparseArray<>();
    private SparseArray<Lottery> lotteryMap = new SparseArray<>();

    public UserCentre(Context context, String serverBase) {
        this.context = context;
        this.serverBase = serverBase;
        session = Preferences.getString(context, KEY_SESSION, null);
        userInfo = GsonHelper.fromJson(Preferences.getString(context, KEY_INFO, null), UserInfo.class);
        userID = userInfo == null ? "unknown" : String.valueOf(userInfo.getId());
        loadLotteryList();
    }

    public LoginCommand createLoginCommand() {
        String name = Preferences.getString(context, KEY_LOGIN_NAME, null);
        String password = Preferences.getString(context, KEY_LOGIN_PASSWORD, null);
        if (name == null || password == null) {
            return null;
        }

        LoginCommand command = new LoginCommand();
        command.setUsername(name);
        command.setPassword(password);
        command.setTerminal_id(2);
        return command;
    }

    public boolean isLogin() {
        return session != null && XinLeApp.getUserCentre().getUserName() != null && getUserInfo() != null;
    }

    public void saveSession(String session) {
        this.session = session;
        if (session == null) {
            Preferences.delete(context, KEY_SESSION);
        } else {
            Preferences.saveString(context, KEY_SESSION, session);
        }
    }

    public void saveLoginInfo(String name, String password) {
        if (name == null || password == null) {
            Preferences.delete(context, KEY_LOGIN_NAME);
            Preferences.delete(context, KEY_LOGIN_PASSWORD);
        } else {
            Preferences.saveString(context, KEY_LOGIN_NAME, name);
            Preferences.saveString(context, KEY_LOGIN_PASSWORD, password);
        }
    }

    public String getSession() {
        return session;
    }

    public String getUserID() {
        return userID;
    }

    public String getKeyApiKey() {
        if (XinLeApp.SERVER_TYPE) {
            return "4394cc50ba594aa275118d947a6dca8f";
        } else {
            return "5ae813185094fdffe94cc9b527353437";
        }
    }

    public String getUrl(String interfaceName) {
        if (interfaceName.startsWith("http://") || interfaceName.startsWith("https://")) {
            return interfaceName;
        }

        if (TextUtils.isEmpty(interfaceName)) {
            return serverBase;
        }
        StringBuffer sb = new StringBuffer(serverBase);
        if (!serverBase.endsWith("/")) {
            sb.append("/");
        }
        if (interfaceName.startsWith("/")) {
            sb.append(interfaceName.substring(1));
        } else {
            sb.append(interfaceName);
        }
        return sb.toString();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getUserName() {
        if (userInfo != null) {
            return userInfo.getUsername();
        }
        return Preferences.getString(context, KEY_LOGIN_NAME, null);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;

        if (userInfo == null) {
            userID = "unknown";
            MobclickAgent.onProfileSignOff();
            Preferences.delete(context, KEY_INFO);
        } else {
            MobclickAgent.onProfileSignIn(userID);
            userID = String.valueOf(userInfo.getId());
            if (userInfo.getToken() == null || userInfo.getToken().length() == 0) {
                userInfo.setToken(getSession());
            }
            Preferences.saveString(context, KEY_INFO, GsonHelper.toJson(userInfo));
        }
    }

    public void logout() {
        saveSession(null);
        setUserInfo(null);
        Preferences.delete(context, KEY_LOGIN_PASSWORD);
        ShoppingCart.getInstance().clear();
    }

    public void setLotteryList(List<Lottery> lotteryList) {
        if (lotteryList == null) {
            Preferences.delete(context, KEY_LOTTERY_MAP);
        } else {
            Preferences.saveString(context, KEY_LOTTERY_MAP, GsonHelper.toJson(lotteryList));
            lotteryMap.clear();
            for (Lottery lottery : lotteryList) {
                lotteryMap.put(lottery.getId(), lottery);
            }
        }
    }

    public ArrayList<Lottery> getLotteryList() {
        TypeToken typeToken = new TypeToken<ArrayList<Lottery>>() {
        };
        return GsonHelper.fromJson(Preferences.getString(context, KEY_LOTTERY_MAP, null), typeToken.getType());
    }

    private void loadLotteryList() {
        TypeToken typeToken = new TypeToken<ArrayList<Lottery>>() {
        };
        ArrayList<Lottery> list = GsonHelper.fromJson(Preferences.getString(context, KEY_LOTTERY_MAP, null), typeToken.getType());
        lotteryMap.clear();
        if (list != null) {
            for (Lottery lottery : list) {
                lotteryMap.put(lottery.getId(), lottery);
            }
        }
    }

    public Lottery getLottery(int lotteryId) {
        return lotteryMap != null ? lotteryMap.get(lotteryId) : null;
    }

    public void setSeriesList(List<Series> seriesList) {
        if (seriesList == null) {
            Preferences.delete(context, KEY_SERIES_MAP);
        } else {
            Preferences.saveString(context, KEY_SERIES_MAP, GsonHelper.toJson(seriesList));
            seriesMap.clear();
            for (int i = 0; i < seriesList.size(); i++) {
                seriesMap.put(i, seriesList.get(i));
            }
        }
    }

    public ArrayList<Series> getSeriesList() {
        TypeToken typeToken = new TypeToken<ArrayList<Series>>() {
        };
        return GsonHelper.fromJson(Preferences.getString(context, KEY_SERIES_MAP, null), typeToken.getType());
    }

    private void loadSeriesList() {
        TypeToken typeToken = new TypeToken<ArrayList<Series>>() {
        };
        ArrayList<Series> list = GsonHelper.fromJson(Preferences.getString(context, KEY_SERIES_MAP, null), typeToken.getType());
        seriesMap.clear();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                seriesMap.put(i, list.get(i));
            }
        }
    }

    public int getLucreMode() {
        return Preferences.getInt(context, KEY_LOTTERY_LUCREMODE, 0);
    }

    public void setLucreMode(int mode) {
        Preferences.saveInt(context, KEY_LOTTERY_LUCREMODE, mode);
    }

    public int getRechargeMode() {
        return Preferences.getInt(context, KEY_LOTTERY_RECHARGE_MODE, 0);
    }

    public void setRechargeMode(int mode) {
        Preferences.saveInt(context, KEY_LOTTERY_RECHARGE_MODE, mode);
    }

    public int getPrizeGroup() {
        return Preferences.getInt(context, KEY_LOTTERY_PRIZEGROUP, 0);
    }

    public void setPrizeGroup(int mode) {
        Preferences.saveInt(context, KEY_LOTTERY_PRIZEGROUP, mode);
    }

    public int getBonusMode(int propertyId) {
        return Preferences.getInt(context, KEY_LOTTERY_BONUSMODE + propertyId, 0);
    }

    public void setBonusMode(int propertyId, int mode) {
        Preferences.saveInt(context, KEY_LOTTERY_BONUSMODE + propertyId, mode);
    }
}
