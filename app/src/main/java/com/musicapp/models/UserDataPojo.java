package com.musicapp.models;

import java.io.Serializable;

public class UserDataPojo implements Serializable {

    UserFiledData user;

    public UserFiledData getUser() {
        return user;
    }

    public void setUser(UserFiledData user) {
        this.user = user;
    }


}
