package com.eiplan.zuji.bean;

//小视频的bean类

import java.io.Serializable;

public class VideoInfo implements Serializable {
    private int id;
    private String title;  //视频名称
    private String introduce;  //视频描述
    private String uploader;//上传者名称
    private String pic;  //上传者头像
    private String phone;
    private String time;//上传时间
    private String cover;//封面地址
    private String play;//播放地址
    private String major;//分类
    private int look;  //浏览次数
    private int good;  //点赞次数
    private int comment;  //评论次数

    public VideoInfo() {
    }

    public VideoInfo(int id, String title, String introduce, String uploader, String pic, String phone, String time, String cover, String play, String major, int look, int good, int comment) {
        this.id = id;
        this.title = title;
        this.introduce = introduce;
        this.uploader = uploader;
        this.pic = pic;
        this.phone = phone;
        this.time = time;
        this.cover = cover;
        this.play = play;
        this.major = major;
        this.look = look;
        this.good = good;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
