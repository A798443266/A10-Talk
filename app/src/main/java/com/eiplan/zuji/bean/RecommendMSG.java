package com.eiplan.zuji.bean;

import java.io.Serializable;

/**
 * 推荐讯息
 */
public class RecommendMSG implements Serializable {

    private int id;
    private String title;
    private String introduce;
    private String content;
    private String pic;
    private String major;

    public RecommendMSG() {
    }

    public RecommendMSG(int id, String title, String introduce, String content, String pic, String major) {
        this.id = id;
        this.title = title;
        this.introduce = introduce;
        this.pic = pic;
        this.content = content;
        this.major = major;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
