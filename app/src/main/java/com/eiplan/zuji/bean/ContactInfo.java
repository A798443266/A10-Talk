package com.eiplan.zuji.bean;

import com.eiplan.zuji.utils.PinYinUtils;

/**
 * 联系人信息bean类
 */
public class ContactInfo {

    private String phone;
    private String name;
    private String pic;
    private String major;

    private String pinyin = "#";//昵称拼音

    public ContactInfo() {
    }

    public ContactInfo(String phone, String name, String pic, String major) {
        this.phone = phone;
        this.name = name;
        this.pic = pic;
        this.major = major;
        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null)
            this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
