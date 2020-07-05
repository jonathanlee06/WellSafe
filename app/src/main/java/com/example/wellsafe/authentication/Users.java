package com.example.wellsafe.authentication;

public class Users {

    public String fullName, email, password, phone;

    public Users(){

    }

    public Users(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

}
