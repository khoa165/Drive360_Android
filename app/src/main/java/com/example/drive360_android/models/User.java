package com.example.drive360_android.models;

import com.example.drive360_android.auth.PasswordHash;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String password;
    public int age;
    public boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        hashPassword(password);
        this.age = -1;
        this.isAdmin = false;
    }

    public String getUsername() {
        return this.username;
    }

    private void hashPassword(String password) {
        try {
            this.password = PasswordHash.hashWithoutSalt(password);
        } catch (Exception e) {
            this.password = password;
        }
    }
}
