package com.eiplan.zuji.bean;

/**
 * 交易记录
 */
public class TradeInfo {
    private int id;
    private String operation;
    private String major;
    private String expert;
    private String user;
    private String time;
    private float price;

    public TradeInfo() {
    }

    public TradeInfo(int id, String operation, String major, String expert, String user, String time, float price) {
        this.id = id;
        this.operation = operation;
        this.major = major;
        this.expert = expert;
        this.user = user;
        this.time = time;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
