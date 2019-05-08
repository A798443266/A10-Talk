package com.eiplan.zuji.bean;

/**
 * 搜索的群信息类
 */
public class SearchGroupInfo {
    private String id;
    private String name;
    private String major;
    private String describe;
    private String pic;

    public SearchGroupInfo() {
    }

    public SearchGroupInfo(String id, String name, String major, String describe, String pic) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.describe = describe;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
