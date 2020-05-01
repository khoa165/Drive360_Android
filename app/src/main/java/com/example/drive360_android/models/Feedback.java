package com.example.drive360_android.models;

public class Feedback {
    public String user;
    public String category;
    public String message;
    public float rating;

    public Feedback(String user, String category, String message, float rating) {
        this.user = user;
        this.category = category;
        this.message = message;
        this.rating = rating;
    }
}
