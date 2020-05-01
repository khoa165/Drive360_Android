package com.example.drive360_android.forms;

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

import com.example.drive360_android.R;
import com.example.drive360_android.models.Question;
import com.example.drive360_android.pages.TestActivity;
import com.example.drive360_android.pages.TestQuestionsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference userQuestionRef;
    private Spinner spinner;
    private String testId;
    private boolean validAnswer;
    private static final String[] answerChoices
            = {"Please set the correct answer", "A", "B", "C", "D"};
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        // Initialize database, root and question references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();

        sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        Intent intent = getIntent();

        String username = sharedPreferences.getString("username", "");
        testId = sharedPreferences.getString("testId", "");

        if (testId.equals("")) {
            goToTestScreen();
        }

        userQuestionRef = rootRef.child("user_tests").child(username).child(testId).child("questions");

        setupSpinner();
    }

    public void setupSpinner () {
        spinner = findViewById(R.id.questionCorrectAnswer);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, answerChoices);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                validAnswer = false;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                validAnswer = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void createQuestion(View v) {
        // Construct question from user inputs.
        Question question = constructQuestion();

        if (question != null) {
            // Generate id;
            String id = userQuestionRef.push().getKey();
            // Send data to questions branch on Firebase.
            userQuestionRef.child(id).setValue(question);

            // Redirect back to add question screen.
            goToTestQuestionScreen();
        }
    }

    // Validate user input and return test object if valid, otherwise null.
    private Question constructQuestion() {
        // Get question name from text field.
        EditText nameInput = findViewById(R.id.questionName);
        String name = nameInput.getText().toString().trim();

        // Get question first choice from text field.
        EditText firstChoiceInput = findViewById(R.id.questionFirstChoice);
        String firstChoice = firstChoiceInput.getText().toString().trim();

        // Get question second choice from text field.
        EditText secondChoiceInput = findViewById(R.id.questionSecondChoice);
        String secondChoice = secondChoiceInput.getText().toString().trim();

        // Get question third choice from text field.
        EditText thirdChoiceInput = findViewById(R.id.questionThirdChoice);
        String thirdChoice = thirdChoiceInput.getText().toString().trim();

        // Get question fourth choice from text field.
        EditText fourthChoiceInput = findViewById(R.id.questionFourthChoice);
        String fourthChoice = fourthChoiceInput.getText().toString().trim();

        // Get correct answer from spinner/dropdown.
        Spinner spinner = findViewById(R.id.questionCorrectAnswer);
        String correctAnswer = spinner.getSelectedItem().toString().trim();

        // Check for valid input.
        if (!name.equals("") && !firstChoice.equals("") && !secondChoice.equals("")
                && !thirdChoice.equals("") && !fourthChoice.equals("") && validAnswer) {
            // Add all answer choices to list.
            List<String> answerChoices = new ArrayList<String>();
            answerChoices.add(firstChoice);
            answerChoices.add(secondChoice);
            answerChoices.add(thirdChoice);
            answerChoices.add(fourthChoice);

            // Construct test object.
            return new Question(name, correctAnswer, answerChoices);
        } else {
            // Notify invalid input using toast and return null.
            Toast.makeText(this, "Please make sure to fill out all fields!", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    // Transition to test screen.
    public void goToTestScreen() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void goToTestQuestionScreen() {
        Intent intent = new Intent(this, TestQuestionsActivity.class);
        startActivity(intent);
    }
}
