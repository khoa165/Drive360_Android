package com.example.drive360_android.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drive360_android.MainActivity;
import com.example.drive360_android.R;
import com.example.drive360_android.models.Tip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AddTipActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference tipRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        // Initialize database, root and feedback references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        tipRef = rootRef.child("tips");
    }

    // Handle tip submission.
    public void submitTip(View v) {
        // Construct tip from user inputs.
        Tip tip = constructTip();

        if (tip != null) {
            // Generate id;
            String id = tipRef.push().getKey();
            // Send data to tips branch on Firebase.
            tipRef.child(id).setValue(tip);
            // Redirect the user to main screen.
            goToMainScreen();
        }
    }

    // Validate user input and return tip object if valid, otherwise null.
    private Tip constructTip() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        // Get current user's username.
        String username = sharedPreferences.getString("username", "");

        // Get tip text from text field.
        EditText textInput = findViewById(R.id.tipText);
        String text = textInput.getText().toString().trim();

        // Check for valid input.
        if (text != null && !text.equals("")) {
            // Construct tip object.
            return new Tip(username, text);
        } else {
            // Notify invalid input using toast and return null.
            Toast.makeText(this, "Please make sure to fill out tip!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    // Redirect the user to main screen.
    public void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
