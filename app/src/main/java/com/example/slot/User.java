package com.example.slot;

public class User {
    private String mail, UserID, name, permission;

    public User(){

    }


    public User(String m, String u, String n, String p){
        mail=m;
        UserID=u;
        name=n;
        permission=p;
    }

    public String getMail() {
        return mail;
    }

    public String getUserID() {
        return UserID;
    }

    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }
}
