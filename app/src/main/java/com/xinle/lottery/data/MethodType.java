package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * 玩法类型
 * Created by ACE-PC on 2016/1/22.
 */
public class MethodType {
    /**
     * id : 4
     * pid : 2
     * name_cn : 直选
     * name_en : zhixuan
     */

    @SerializedName("id")
    private int id;
    @SerializedName("pid")
    private int pid;
    @SerializedName("name_cn")
    private String nameCn;
    @SerializedName("name_en")
    private String nameEn;
    public MethodType(){
    }

    public MethodType(int id, int pid, String name_cn, String name_en) {
        this.id = id;
        this.pid = pid;
        this.nameCn = name_cn;
        this.nameEn = name_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
