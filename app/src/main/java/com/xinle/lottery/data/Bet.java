package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Alashi on 2016/1/22.
 */
public class Bet {
    /**
     * id : 21826
     * user_id : 284
     * terminal_id : null
     * serial_number : 284574810D6B13DE7.62793641
     * trace_id : null
     * prize_group : 1950
     * lottery_id : 1
     * issue : 160527068
     * way_id : 69
     * way : 后三直选复式
     * bet_number : 4|7|9
     * multiple : 1
     * coefficient : 0.010
     * amount : 0.0200
     * winning_number : null
     * prize : null
     * status : 0
     * bought_at : 2016-05-27 17:18:14
     */

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("terminal_id")
    private String terminalId;
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("trace_id")
    private int traceId;
    @SerializedName("prize_group")
    private int prizeGroup;
    @SerializedName("lottery_id")
    private int lotteryId;
    @SerializedName("issue")
    private String issue;
    @SerializedName("way_id")
    private int wayId;
    @SerializedName("way")
    private String way;
    @SerializedName("bet_number")
    private String betNumber;
    @SerializedName("multiple")
    private int multiple;
    @SerializedName("coefficient")
    private float coefficient;
    @SerializedName("amount")
    private double amount;
    @SerializedName("winning_number")
    private String winningNumber;
    @SerializedName("prize")
    private String prize;
    @SerializedName("status")
    private int status;
    @SerializedName("bought_at")
    private Date boughtAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getTraceId() {
        return traceId;
    }

    public void setTraceId(int traceId) {
        this.traceId = traceId;
    }

    public int getPrizeGroup() {
        return prizeGroup;
    }

    public void setPrizeGroup(int prizeGroup) {
        this.prizeGroup = prizeGroup;
    }

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getWayId() {
        return wayId;
    }

    public void setWayId(int wayId) {
        this.wayId = wayId;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getBetNumber() {
        return betNumber;
    }

    public void setBetNumber(String betNumber) {
        this.betNumber = betNumber;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getWinningNumber() {
        return winningNumber;
    }

    public void setWinningNumber(String winningNumber) {
        this.winningNumber = winningNumber;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getBoughtAt() {
        return boughtAt;
    }

    public void setBoughtAt(Date boughtAt) {
        this.boughtAt = boughtAt;
    }
}
