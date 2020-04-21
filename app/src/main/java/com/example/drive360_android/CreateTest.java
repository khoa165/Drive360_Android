package com.example.drive360_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTest extends AppCompatActivity {
    public void onSubmitClicked(View view){
        goToCreateTest();
    }
    public void goToCreateTest(){
        Intent intent = new Intent(this, TestQuestions.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
    }
}
