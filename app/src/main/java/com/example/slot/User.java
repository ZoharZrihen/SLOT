package com.example.slot;

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
}
