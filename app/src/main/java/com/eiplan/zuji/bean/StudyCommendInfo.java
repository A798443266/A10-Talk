package com.eiplan.zuji.bean;

/**
 * 课程评论信息类
 */
public class StudyCommendInfo {
    private int vedioId;
    private String userName;
    private String time;
    private String userPic;
    private String content;
    private int good;
    private int replay;

    public StudyCommendInfo() {
    }

    public StudyCommendInfo(int vedioId, String userName, String time, String userPic, String content, int good, int replay) {
        this.vedioId = vedioId;
        this.userName = userName;
        this.time = time;
        this.userPic = userPic;
        this.content = content;
        this.good = good;
        this.replay = replay;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getReplay() {
        return replay;
    }

    public void setReplay(int replay) {
        this.replay = replay;
    }
}
