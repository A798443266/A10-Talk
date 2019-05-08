package com.eiplan.zuji.bean;

import com.eiplan.zuji.utils.PinYinUtils;

import java.io.Serializable;

// 用户账号信息的bean类
public class UserInfo implements Serializable {

    private String name;// 用户名称
    private String phone;// 手机号
    private String pic;// 头像地址
    private String company;//公司
    private String industry;//行业
    private int balance;//余额
    private int bankcard;//银行卡
    private int point;//积分
    private int isExpert = 0;//是否为专家

    private String pinyin = "#";//昵称拼音

    public UserInfo() {
    }

    public UserInfo(String name, String phone, String pic, String company, String industry, int balance, int bankcard, int point, int isExpert) {
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.company = company;
        this.balance = balance;
        this.bankcard = bankcard;
        this.point = point;
        this.isExpert = isExpert;
        this.industry = industry;

        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null)
            this.pinyin = PinYinUtils.getPinYin(name);
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBankcard() {
        return bankcard;
    }

    public void setBankcard(int bankcard) {
        this.bankcard = bankcard;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int isExpert() {
        return isExpert;
    }

    public void setExpert(int expert) {
        isExpert = expert;
    }

    public int getIsExpert() {
        return isExpert;
    }

    public void setIsExpert(int isExpert) {
        this.isExpert = isExpert;
    }
}
