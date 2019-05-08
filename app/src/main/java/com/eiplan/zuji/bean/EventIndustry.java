package com.eiplan.zuji.bean;

public class EventIndustry {
    private int first;  //第一大类
    private int sencond;  //第二小类

    public EventIndustry(int first, int sencond) {
        this.first = first;
        this.sencond = sencond;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSencond() {
        return sencond;
    }

    public void setSencond(int sencond) {
        this.sencond = sencond;
    }
}
