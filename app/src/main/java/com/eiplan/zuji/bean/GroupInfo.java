package com.eiplan.zuji.bean;

/**
 * Created by Administrator on 2016/9/24.
 */
// 群信息的bean类
public class GroupInfo {
    private String groupName;   // 群名称
    private String groupId;     // 群id
    private String invatePerson;// 邀请人
    private String pic ;//邀请人头像地址

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String invatePerson,String pic) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invatePerson = invatePerson;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInvatePerson() {
        return invatePerson;
    }

    public void setInvatePerson(String invatePerson) {
        this.invatePerson = invatePerson;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", invatePerson='" + invatePerson + '\'' +
                '}';
    }
}
