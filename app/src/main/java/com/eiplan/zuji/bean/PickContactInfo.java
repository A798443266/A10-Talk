package com.eiplan.zuji.bean;

// 选择邀请进入群组联系人的bean类
public class PickContactInfo {
    private ContactInfo contact;      // 联系人
    private boolean isChecked;  // 是否被选择的标记

    public PickContactInfo(ContactInfo contact, boolean isChecked) {
        this.contact = contact;
        this.isChecked = isChecked;
    }

    public PickContactInfo() {
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
