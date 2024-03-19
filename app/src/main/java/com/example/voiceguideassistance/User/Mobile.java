package com.example.voiceguideassistance.User;

public class Mobile {
    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public Mobile(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String name;
    public String mobile;
}
