package com.example.drive360_android.pages;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Arrays;
import java.util.List;

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
        //dumby values
        questionCount = 25;
        averageRating = (float) 4.5;

        // Initialize database, root and feedback references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        userRef = rootRef.child("users");
        feedbackRef = rootRef.child("feedbacks");
        generateUserCount();

        ListView lv = (ListView) findViewById(R.id.feedbackList);
        // Need to get feedback from fire base !!!!! (Harry)
        // Create a List of feedback
        List<String> feedbackList = new ArrayList<String>();

        // Procedure to change color of list view items
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, feedbackList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#CC0C0C"));

                // Generate ListView Item using TextView
                return view;
            }
        };
        TextView countQuestions = findViewById(R.id.questionCount);
        countQuestions.setText("Total number of questions: " + questionCount);

        TextView aveRating = findViewById(R.id.averageRating);
        //Need to put a list in the below line of all the ratings !!! (Harry)
        //List<Float> ratings = new ArrayList<Float>();
        //calculateAverageRating(ratings);
        aveRating.setText("Average App rating: " + averageRating);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
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
        userRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                TextView userCount = findViewById(R.id.userCount);
                userCount.setText("Total number of users: " + count);
            }
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
