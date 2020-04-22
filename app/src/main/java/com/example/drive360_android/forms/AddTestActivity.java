package com.example.drive360_android.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drive360_android.R;
import com.example.drive360_android.TestQuestions;

public class AddTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    public void createTest(View view){
        goToCreateTest();
    }
    public void goToCreateTest(){
        Intent intent = new Intent(this, TestQuestions.class);
        startActivity(intent);
    }
}
