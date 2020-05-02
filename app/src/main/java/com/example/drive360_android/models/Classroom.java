package com.example.drive360_android.models;

import java.util.List;

public class Classroom {
    public String instructor;
    public List<String> learners;
    public String name;
    public String description;
    public String website;

    public Classroom() {
    }

    public Classroom(String instructor, List<String> learners, String name, String description, String website) {
        this.instructor = instructor;
        this.learners = learners;
        this.name = name;
        this.description = description;
        this.website = website;
    }
}
