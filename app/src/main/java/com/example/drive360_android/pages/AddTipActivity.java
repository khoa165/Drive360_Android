package com.example.drive360_android.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drive360_android.MainActivity;
import com.example.drive360_android.R;
import com.example.drive360_android.models.Tip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AddTipActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference tipRef;
    private Spinner spinner;
    private boolean validTipCategory;
    private static final String[] tipCategories
            = {"Please choose tip category", "For beginners", "Traffic laws", "Driving warnings", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        // Initialize database, root and feedback references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        tipRef = rootRef.child("tips");

        setupSpinner();
    }

    public void setupSpinner () {
        spinner = findViewById(R.id.tipCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                validTipCategory = false;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                validTipCategory = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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

        // Get tip category from spinner/dropdown.
        Spinner spinner = findViewById(R.id.tipCategory);
        String category = spinner.getSelectedItem().toString().trim();

        // Get tip text from text field.
        EditText textInput = findViewById(R.id.tipText);
        String text = textInput.getText().toString().trim();

        // Check for valid input.
        if (text != null && !text.equals("")) {
            // Construct tip object.
            return new Tip(username, text, category);
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
