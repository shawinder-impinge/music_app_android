package com.impinge.soul.models;

import java.io.Serializable;

public class ChangePassModel implements Serializable {
    private String valid;
    private String code,message;

    public ChangePassModel(String valid, String code, String message) {
        this.valid = valid;
        this.code = code;
        this.message = message;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
