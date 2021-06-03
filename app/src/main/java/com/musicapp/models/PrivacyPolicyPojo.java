package com.musicapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivacyPolicyPojo implements Serializable {
    ArrayList<PrivacyPage> page;

    public ArrayList<PrivacyPage> getPage() {
        return page;
    }

    public void setPage(ArrayList<PrivacyPage> page) {
        this.page = page;
    }
}
