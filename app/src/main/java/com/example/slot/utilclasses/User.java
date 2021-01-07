package com.example.slot.utilclasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String mail, userID, name, permission;

    public User(){

    }


    public User(String m, String u, String n, String p){
        mail= m;
        userID = u;
        name= n;
        permission = p;

    }

    public String getMail() {
        return mail;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
