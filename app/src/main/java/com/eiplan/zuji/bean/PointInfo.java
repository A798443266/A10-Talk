package com.eiplan.zuji.bean;

/**
 * 积分类
 */
public class PointInfo {
    private int id;
    private String point;
    private String time;
    private String content;

    public PointInfo() {
    }

    public PointInfo(int id, String point, String time, String content) {
        this.id = id;
        this.point = point;
        this.time = time;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
