package com.eiplan.zuji.bean;

import java.io.Serializable;

public class DemandInfo implements Serializable {
    private int id;
    private String expert;//要找的专家
    private String phone;
    private String company;
    private String city;
    private String background;//背景
    private String details; //详情
    private String requirement; //要求
    private String validity;  //有效期
    private String budget;  //预算
    private int look;  //浏览次数
    private String time; //发布时间
    private int state;//状态

    public DemandInfo() {
    }

    public DemandInfo(int id, String expert, String phone, String company, String city, String background,
                      String details, String requirement, String validity, String budget,
                      int look, String time,int state) {
        this.id = id;
        this.expert = expert;
        this.phone = phone;
        this.company = company;
        this.city = city;
        this.background = background;
        this.details = details;
        this.requirement = requirement;
        this.validity = validity;
        this.budget = budget;
        this.look = look;
        this.time = time;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
