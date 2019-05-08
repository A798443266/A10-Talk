package com.eiplan.zuji.bean;

import java.io.Serializable;

/**
 * 文档信息类
 */
public class DocInfo implements Serializable {
    private int id;
    private String name; //名称
    private String phone; //名称
    private String pic; //作者头像
    private String major;//分类
    private String writer;//作者名称
    private String updatetime; //更新时间
    private int buy; //购买次数
    private float price; //价格
    private String introduce; //简介
    private int good; //点赞次数
    private int point; //积分
    private String view; //预览地址
    private String link; //下载地址

    public DocInfo() {
    }

    public DocInfo(int id, String name, String phone, String pic, String major, String writer, String updatetime,
                   int buy, float price, String introduce, int good, int point, String view, String link) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.major = major;
        this.writer = writer;
        this.updatetime = updatetime;
        this.buy = buy;
        this.price = price;
        this.introduce = introduce;
        this.good = good;
        this.point = point;
        this.view = view;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
