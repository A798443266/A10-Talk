package com.eiplan.zuji.menu;

/**
 * 二级分类，相当于右侧菜单
 */
public class SecondItem {
    private int id;
    private String name;

    public SecondItem() {

    }

    public SecondItem(int id, String name) {
        this.id = id;
        this.name = name;
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
}
