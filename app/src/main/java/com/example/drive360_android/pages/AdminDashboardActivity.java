package com.example.drive360_android.pages;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drive360_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference feedbackRef;
    private float averageRating;
    private int questionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize database, root and feedback references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        userRef = rootRef.child("users");
        feedbackRef = rootRef.child("feedbacks");
        generateUserCount();
    }
    private void calculateQuestionCount(){
    }

    private void calculateAverageRating(ArrayList<Float> ratings){
        float averageRating = 0;
        for(Float rating : ratings){
            averageRating += rating;
        }
        averageRating /= ratings.size();
    }
    private void generateUserCount() {
        questionCount = 25;
        averageRating = (float) 4.5;

        userRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                TextView userCount = findViewById(R.id.userCount);
                userCount.setText("Total number of users: " + count);
            }
            public void onCancelled(DatabaseError databaseError) { }
        });
        TextView countQuestions = findViewById(R.id.questionCount);
        countQuestions.setText("Total number of questions: " + questionCount);

        TextView aveRating = findViewById(R.id.averageRating);
        //calculateAverageRating();
        aveRating.setText("Average App rating: " + averageRating);
    }
}
