package com.example.drive360_android.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.drive360_android.R;
import com.example.drive360_android.models.Feedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.drive360_android.Config.appStatsRef;
import static com.example.drive360_android.Config.feedbacksRef;

public class AppStatsActivity extends AppCompatActivity {
    private long userCount = 0;
    private long feedbackCount = 0;
    private long questionCount = 0;
    private long tipCount = 0;
    private double appRating = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_stats);

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

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

        generateUserCount();
        generateTipCount();
        generateFeedbackCount();
        generateQuestionCount();
    }


    private void generateUserCount() {
        appStatsRef.child("num_users").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userCount = (Long) dataSnapshot.getValue();
                    TextView tv = findViewById(R.id.userCount);
                    tv.setText("Total number of users: " + userCount);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void generateTipCount() {
        appStatsRef.child("num_tips").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tipCount = (Long) dataSnapshot.getValue();
                    TextView tv = findViewById(R.id.tipCount);
                    tv.setText("Total number of tips: " + tipCount);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void generateFeedbackCount() {
        appStatsRef.child("num_feedbacks").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    feedbackCount = (Long) dataSnapshot.getValue();
                    TextView tv = findViewById(R.id.feedbackCount);
                    tv.setText("Total number of feedbacks: " + feedbackCount);
                    generateAppRating();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void generateQuestionCount() {
        appStatsRef.child("num_questions").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    questionCount = (Long) dataSnapshot.getValue();
                    TextView tv = findViewById(R.id.questionCount);
                    tv.setText("Total number of questions: " + questionCount);
                    generateAppRating();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void generateAppRating() {
        feedbacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Feedback feedback = d.getValue(Feedback.class);
                        appRating += feedback.rating;
                    }
                    if (feedbackCount > 0) {
                        appRating /= feedbackCount;
                        TextView tv = findViewById(R.id.averageRating);
                        tv.setText("Average app rating: " + appRating);
                    }

                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
