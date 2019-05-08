package com.eiplan.zuji.bean;

/**
 * 视频评论信息的bean类
 */

public class CommentInfo {
    private int vedioId;  //对应的视频id
    private String userName; //用户名称
    private String userPic; //用户头像
    private String time; //时间
    private String content; //平路内容
    private int reply;
    private int good; //点赞次数

    public CommentInfo() {
    }

    public CommentInfo(int vedioId, String userName, String userPic, String time, String content, int reply, int good) {
        this.vedioId = vedioId;
        this.userName = userName;
        this.userPic = userPic;
        this.time = time;
        this.content = content;
        this.reply = reply;
        this.good = good;
    }

    public int getVedioId() {
        return vedioId;
    }

    public void setVedioId(int vedioId) {
        this.vedioId = vedioId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
