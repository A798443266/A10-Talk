package com.eiplan.zuji.bean;

/**
 * 问题评论的bean类
 */
public class ProblemCommentInfo {
    private int forumId;  //对应的视频id
    private String name; //用户名称
    private String phone; //用户手机号
    private String pic;//用户头像
    private String time; //时间
    private String content; //评论内容
    private int reply;
    private int good; //点赞次数
    private String state; //是否采纳

    public ProblemCommentInfo() {
    }

    public ProblemCommentInfo(int forumId, String name, String phone, String pic, String time, String content, int reply, int good, String state) {
        this.forumId = forumId;
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.time = time;
        this.content = content;
        this.reply = reply;
        this.good = good;
        this.state = state;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
