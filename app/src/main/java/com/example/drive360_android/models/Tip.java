package com.example.drive360_android.models;

import java.util.ArrayList;
import java.util.List;

public class Tip {
    public String username;
    public String text;
    public String category;
    public int votes;
    public List<String> voters = new ArrayList<String>();

    public Tip(String username, String text, String category) {
        this.username = username;
        this.text = text;
        this.category = category;
        this.votes = 1;
        this.voters.add(username);
    }
}
