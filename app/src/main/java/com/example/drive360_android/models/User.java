package com.example.drive360_android.models;

import com.example.drive360_android.auth.PasswordHash;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private String salt;
    private int age;
    private boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        hashPassword(password);
        this.age = -1;
        this.isAdmin = false;
    }

    public User(String username, String password, int age) {
        this(username, password);
        this.age = age;
    }

    public String getUsername() {
        return this.username;
    }

    private void hashPassword(String password) {
        try {
            byte[] salt = PasswordHash.getSalt();
            this.password = PasswordHash.hashWithoutSalt(password);
        } catch (Exception e) {
            this.password = password;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        addField(result, "username", username);
        addField(result, "password", password);
        addField(result, "salt", salt);
        addField(result, "age", age);
        addField(result, "isAdmin", isAdmin);
        return result;
    }

    private void addField(Map<String, Object> map, String field, Object value) {
        if (value != null) {
            map.put(field, value);
        }
    }
}
