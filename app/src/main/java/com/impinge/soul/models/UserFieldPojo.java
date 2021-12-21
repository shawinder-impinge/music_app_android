package com.impinge.soul.models;

import java.io.Serializable;

public class UserFieldPojo implements Serializable {
    UserFiledData user;

    public UserFiledData getUser() {
        return user;
    }

    public void setUser(UserFiledData user) {
        this.user = user;
    }
}
