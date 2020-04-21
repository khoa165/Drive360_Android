package com.example.drive360_android.models;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
    public String username;
    public String category;
    public String message;
    public float rating;

    public Feedback(String username, String category, String message, float rating) {
        this.username = username;
        this.category = category;
        this.message = message;
        this.rating = rating;
    }
}
