package com.eiplan.zuji.bean;

import java.io.Serializable;

public class ExpertInfo implements Serializable {
    private String phone;//电话号码，环信id
    private String password;//密码
    private String name;//名称
    private String pic;//头像地址

    private String job;//职位
    private String workyear;//工作经验
    private String major;//行业
    private String introduce;//简介
    private String address;//城市

    private int balance;//余额
    private int point;//积分
    private int look;//浏览次数
    private int ask; //咨询
    private float evaluate;//评分

    public ExpertInfo() {
    }

    public ExpertInfo(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public ExpertInfo(String phone, String password, String name, String pic, String job, String workyear,
                      String major, String introduce, String address, int balance, int point, int look,
                      int ask, float evaluate) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.pic = pic;
        this.job = job;
        this.workyear = workyear;
        this.major = major;
        this.introduce = introduce;
        this.address = address;
        this.balance = balance;
        this.point = point;
        this.look = look;
        this.ask = ask;
        this.evaluate = evaluate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWorkyear() {
        return workyear;
    }

    public void setWorkyear(String workyear) {
        this.workyear = workyear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAsk() {
        return ask;
    }

    public void setAsk(int ask) {
        this.ask = ask;
    }

    public float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(float evaluate) {
        this.evaluate = evaluate;
    }
}
