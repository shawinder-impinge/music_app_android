package com.musicapp.models;

import java.io.Serializable;

public class User implements Serializable {

    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
