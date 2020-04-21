package com.example.drive360_android.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tip {
    public String username;
    public String text;
    public int votes;
    public Set<String> voters = new HashSet<String>();

    public Tip(String username, String text, int votes) {
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
