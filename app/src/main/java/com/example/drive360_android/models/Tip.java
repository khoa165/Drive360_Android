package com.example.drive360_android.models;

import java.util.ArrayList;
import java.util.List;

public class Tip {
    public String username;
    public String text;
    public int votes;
    public List<String> voters = new ArrayList<String>();

    public Tip(String username, String text) {
        this.username = username;
        this.text = text;
        this.votes = 1;
        this.voters.add(username);
    }

//    public Map<String, Object> toMap() {
//        Map<String, Object> result = new HashMap<>();
//        result.put("username", username);
//        result.put("text", text);
//
//        Map<String, Object> users = new HashMap<>();
//        for (String voter : voters) {
//            users.put()
//        }
//        return result;
//    }
}
