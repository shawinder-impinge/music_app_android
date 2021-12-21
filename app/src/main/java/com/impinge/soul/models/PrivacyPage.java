package com.impinge.soul.models;

import java.io.Serializable;

public class PrivacyPage implements Serializable {
    int id;
    String title,pagerole,description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPagerole() {
        return pagerole;
    }

    public void setPagerole(String pagerole) {
        this.pagerole = pagerole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
