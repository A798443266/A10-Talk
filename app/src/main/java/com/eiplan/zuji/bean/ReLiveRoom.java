package com.eiplan.zuji.bean;

import java.io.Serializable;

/**
 * 直播回放的类
 */
public class ReLiveRoom implements Serializable {
    private int id;
    private String name;
    private String toupic;
    private String major;
    private String cover;
    private String title;
    private String look;
    private String playurl;

    public ReLiveRoom() {
    }

    public ReLiveRoom(int id, String name, String toupic, String major, String cover, String title, String look, String playurl) {
        this.id = id;
        this.name = name;
        this.toupic = toupic;
        this.major = major;
        this.cover = cover;
        this.title = title;
        this.look = look;
        this.playurl = playurl;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }
}
