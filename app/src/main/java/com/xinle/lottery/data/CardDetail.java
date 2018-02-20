package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * 银行卡信息
 * Created by Alashi on 2016/3/17.
 */
public class CardDetail {

    /**
     * bind_card_id : 18
     * user_id : 56927
     * username : alashi
     * bank_id : 1
     * card_num : ****************004
     * province : 海南
     * city : 屯昌县
     * branch : 开户点名称xxx111
     * create_time : 2016-03-17 15:59:04
     * status : 1
     * finish_admin_id : 0
     * remark :
     * ts : 2016-03-17 15:59:04
     */

    @SerializedName("bind_card_id")
    private int bindCardId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("username")
    private String userName;
    @SerializedName("bank_id")
    private int bankId;
    @SerializedName("card_num")
    private String cardNumber;
    private String province;
    private String city;
    private String branch;
    @SerializedName("create_time")
    private String createTime;
    private int status;
    @SerializedName("finish_admin_id")
    private int finishAdminId;
    private String remark;
    private String ts;

    public int getBindCardId() {
        return bindCardId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getBankId() {
        return bankId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getBranch() {
        return branch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getStatus() {
        return status;
    }

    public int getFinishAdminId() {
        return finishAdminId;
    }

    public String getRemark() {
        return remark;
    }

    public String getTs() {
        return ts;
    }
}
