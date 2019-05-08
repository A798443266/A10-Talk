package com.eiplan.zuji.bean;

import java.io.Serializable;

public class StudyInfo implements Serializable {
    private int id;
    private String title;
    private String cover;
    private float price;
    private String plays;
    private String time;

    public StudyInfo() {
    }

    public StudyInfo(int id, String title, String cover, float price, String plays, String time) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.price = price;
        this.plays = plays;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPlays() {
        return plays;
    }

    public void setPlays(String plays) {
        this.plays = plays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
