package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2016/11/18.
 */

public class CardRecharge {
    /**
     * bank_id : 203
     * pay_name : 智付充值
     * card_id : 4
     * shopURL : http://test3rd.jinyazhou88.org/mobileDinPay.php
     */

    @SerializedName("bank_id")
    private int bankId;
    @SerializedName("pay_name")
    private String payName;
    @SerializedName("card_id")
    private String cardId;
    @SerializedName("shopURL")
    private String shopURL;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getShopURL() {
        return shopURL;
    }

    public void setShopURL(String shopURL) {
        this.shopURL = shopURL;
    }
}
