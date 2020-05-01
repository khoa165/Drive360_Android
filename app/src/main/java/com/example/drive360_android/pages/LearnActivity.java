package com.example.drive360_android.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.drive360_android.R;
import com.example.drive360_android.forms.AddTipActivity;

public class LearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
    }

    // Transition to manual screen.
    public void goToManualScreen(View view) {
        Intent intent = new Intent(this, ManualActivity.class);
        startActivity(intent);
    }

    // Transition to test screen.
    public void goToTestScreen(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    // Transition to add tip screen.
    public void goToAddTipScreen(View view) {
        Intent intent = new Intent(this, AddTipActivity.class);
        startActivity(intent);
    }
}
