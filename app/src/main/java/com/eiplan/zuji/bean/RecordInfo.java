package com.eiplan.zuji.bean;

import java.sql.Timestamp;

/**
 * 搜索记录类
 */

public class RecordInfo {
    private String phone;
    private int type;
    private String content;
    private String time;

    public RecordInfo() {
    }

    public RecordInfo(String phone, int type, String content, String time) {
        this.phone = phone;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
