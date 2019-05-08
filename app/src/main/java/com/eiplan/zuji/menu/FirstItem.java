package com.eiplan.zuji.menu;

import java.util.List;

/**
 * 一级分类，相当于左侧菜单
 */
public class FirstItem {
    private int id;
    private String name;
    private List<SecondItem> secondItems;

    public FirstItem() {
    }

    public FirstItem(int id, String name, List<SecondItem> secondItems) {
        this.id = id;
        this.name = name;
        this.secondItems = secondItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SecondItem> getSecondItems() {
        return secondItems;
    }

    public void setSecondItems(List<SecondItem> secondItems) {
        this.secondItems = secondItems;
    }
}
