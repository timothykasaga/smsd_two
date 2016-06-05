package com.victoria.timothykasaga.gorret;

/**
 * Created by Leontymo on 4/21/2016.
 */
public class Admin {
    String email;
    String name;
    String pass;
    String tel;
    String username;

    public Admin(String username, String password)
    {
        this.username = username;
        this.pass = password;
    }

    public Admin(String name, String email, String username, String password, String telno)
    {
        this.name = name;
        this.email = email;
        this.username = username;
        this.pass = password;
        this.tel = telno;

    }
}
