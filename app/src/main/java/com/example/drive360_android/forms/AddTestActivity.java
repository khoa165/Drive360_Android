package com.example.drive360_android.forms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drive360_android.R;
import com.example.drive360_android.pages.TestQuestionsActivity;
import com.example.drive360_android.models.Test;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTestActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference userTestRef;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        // Initialize database, root and test references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        userTestRef = rootRef.child("user_tests");

        sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);
    }

    public void createTest(View view) {
        // Construct test from user inputs.
        Test test = constructTest();

        if (test != null) {
            // Generate id;
            String id = userTestRef.push().getKey();
            // Send data to tests branch on Firebase.
            userTestRef.child(test.author).child(id).setValue(test);

            sharedPreferences.edit().putString("testId", id).apply();

            // Transition the user to add question screen.
            goToTestQuestionScreen();
        }
    }

    // Validate user input and return test object if valid, otherwise null.
    private Test constructTest() {
        // Get current user's username.
        String username = sharedPreferences.getString("username", "");

        // Get test name from text field.
        EditText nameInput = findViewById(R.id.testName);
        String name = nameInput.getText().toString().trim();

        // Get test description from text field.
        EditText descriptionInput = findViewById(R.id.testDescription);
        String description = descriptionInput.getText().toString().trim();

        // Check for valid input.
        if (name != null && !name.equals("") && description != null && !description.equals("")) {
            // Construct test object.
            return new Test(username, false, name, description);
        } else {
            // Notify invalid input using toast and return null.
            Toast.makeText(this, "Please make sure to fill out name and description!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void goToTestQuestionScreen() {
        Intent intent = new Intent(this, TestQuestionsActivity.class);
        startActivity(intent);
    }
}
