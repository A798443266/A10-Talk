package com.eiplan.zuji.bean;

/**
 * 搜索的联系人信息类
 */
public class SearchContactInfo {
    private String name;
    private String major;
    private String phone;
    private String pic;

    public SearchContactInfo() {
    }

    public SearchContactInfo(String name, String major, String phone, String pic) {
        this.name = name;
        this.major = major;
        this.phone = phone;
        this.pic = pic;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
