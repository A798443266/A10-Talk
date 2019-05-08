package com.eiplan.zuji.bean;

import java.io.Serializable;

public class LiveRoom implements Serializable {
    private int id; //房间号
    private String pic;//主播头像地址
    private String streamingurl;//推流地址
    private String playurl; //播放地址
    private String title; //标题
    private String content; //直播描述
    private String cover; //封面图片地址
    private String name;//主播名字
    private String major;//主播行业
    private int see;//人数
    private String chatroom;
    private String phone;

    public LiveRoom() {
    }

    public LiveRoom(int id, String pic, String streamingurl, String playurl, String title, String content, String cover, String name, String major, int see, String chatroom, String phone) {
        this.id = id;
        this.pic = pic;
        this.streamingurl = streamingurl;
        this.playurl = playurl;
        this.title = title;
        this.content = content;
        this.cover = cover;
        this.name = name;
        this.major = major;
        this.see = see;
        this.chatroom = chatroom;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStreamingurl() {
        return streamingurl;
    }

    public void setStreamingurl(String streamingurl) {
        this.streamingurl = streamingurl;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getSee() {
        return see;
    }

    public void setSee(int see) {
        this.see = see;
    }

    public String getChatroom() {
        return chatroom;
    }

    public void setChatroom(String chatroom) {
        this.chatroom = chatroom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
