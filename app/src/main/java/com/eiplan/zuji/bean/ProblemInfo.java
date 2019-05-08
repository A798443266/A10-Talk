package com.eiplan.zuji.bean;


import java.io.Serializable;

/**
 * 问题需求的bean类
 */
public class ProblemInfo implements Serializable {
    private int id;
    private String pubisher;//上传者名称
    private String phone;//上传者手机号
    private String toupic; //上传者头像地址
    private String major; //问题分类
    private String question; //问题名称
    private String content; //问题描述
    private String time; //时间
    private int point; //积分
    private String pic = "0"; //如果没有携带图片地址就为0
    private int comment; //评论次数
    private int good; //点赞次数

    public ProblemInfo() {
    }

    public ProblemInfo(int id, String pubisher, String phone, String toupic, String major, String question, String content, String time, int point, String pic, int comment, int good) {
        this.id = id;
        this.pubisher = pubisher;
        this.phone = phone;
        this.toupic = toupic;
        this.major = major;
        this.question = question;
        this.content = content;
        this.time = time;
        this.point = point;
        this.pic = pic;
        this.comment = comment;
        this.good = good;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPubisher() {
        return pubisher;
    }

    public void setPubisher(String pubisher) {
        this.pubisher = pubisher;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToupic() {
        return toupic;
    }

    public void setToupic(String toupic) {
        this.toupic = toupic;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }
}
