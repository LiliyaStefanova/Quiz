package com.liliya.menu;

public class TextMenuItem {

    private String title;
    private MenuActions action;

    public TextMenuItem(String title, MenuActions action) {
        this.title = title;
        this.action = action;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MenuActions getAction() {
        return action;
    }

    public void setAction(MenuActions action) {
        this.action = action;
    }
}