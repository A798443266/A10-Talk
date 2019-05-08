package com.eiplan.zuji.bean;

/**
 * 系统通知的bean类
 */
public class SystemNotificationInfo {
    private int id;
    private String phone;
    private String time;
    private String content;
    private String type;

    public SystemNotificationInfo() {
    }

    public SystemNotificationInfo(int id, String phone, String time, String content, String type) {
        this.id = id;
        this.phone = phone;
        this.time = time;
        this.content = content;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
