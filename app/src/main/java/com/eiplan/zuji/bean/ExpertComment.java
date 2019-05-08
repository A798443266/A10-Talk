package com.eiplan.zuji.bean;

//专家评价的bean类
public class ExpertComment {
    private String expphone;//专家手机号
    private String userName;//评价的用户名称
    private String content;//评价的内容
    private String userPic;//评价的用户头像地址
    private String time;//时间

    public ExpertComment() {
    }

    public ExpertComment(String expphone, String userName, String content, String userPic, String time) {
        this.expphone = expphone;
        this.userName = userName;
        this.content = content;
        this.userPic = userPic;
        this.time = time;
    }

    public String getExpphone() {
        return expphone;
    }

    public void setExpphone(String expphone) {
        this.expphone = expphone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
