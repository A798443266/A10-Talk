package com.eiplan.zuji.bean;

/**
 * 我的专家中的bean类
 */
public class MyExpertInfo {
    private int id; //订单号
    private String uphone;  //用户手机号
    private String ephone; //专家手机号
    private float price;  //价格
    private String state;  //订单状态
    private String name;  //专家名字
    private String job;   //职业
    private String workyear;  //工作年限
    private String major;   //行业
    private String pic;    //专家头像地址
    private String time;   //时间

    public MyExpertInfo() {
    }

    public MyExpertInfo(int id, String uphone, String ephone, float price, String state, String name, String job, String workyear, String major, String pic, String time) {
        this.id = id;
        this.uphone = uphone;
        this.ephone = ephone;
        this.price = price;
        this.state = state;
        this.name = name;
        this.job = job;
        this.workyear = workyear;
        this.major = major;
        this.pic = pic;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getEphone() {
        return ephone;
    }

    public void setEphone(String ephone) {
        this.ephone = ephone;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWorkyear() {
        return workyear;
    }

    public void setWorkyear(String workyear) {
        this.workyear = workyear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
