package com.example.drive360_android.pages;
import static com.example.drive360_android.Config.tipsRef;

import androidx.annotation.NonNull;
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
import static com.example.drive360_android.Config.feedbacksRef;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDisplayActivity extends AppCompatActivity {
    private List<String> feedback;
    private ListView feedbackList;


    private void setupListView() {
        feedback = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, feedback){
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
        feedbacksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Feedback f = d.getValue(Feedback.class);
                        feedback.add("Category: " + f.category + "\nFeedback: " + f.message + " " + f.rating + "\nSubmitted by: " + f.user);
                    }
                    feedbackList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_display);
        feedbackList = findViewById(R.id.feedbackList);
        setupListView();


    }
}


