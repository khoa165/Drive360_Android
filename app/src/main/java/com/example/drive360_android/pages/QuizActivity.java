package com.example.drive360_android.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.drive360_android.R;
import com.example.drive360_android.models.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.drive360_android.Config.userTestsRef;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questions;
    private List<Integer> correctAnswers;
    private DatabaseReference userQuestionRef;
    private String testId;
    private SharedPreferences sharedPreferences;
    private int currentQuestion = 0;
    private boolean firstAttemptUsed = false;

    private TextView questionTitle;
    private Button firstChoiceButton, secondChoiceButton, thirdChoiceButton, fourthChoiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        testId = sharedPreferences.getString("testId", "");

        if (testId.equals("")) {
            goToTestScreen();
        }

        userQuestionRef = userTestsRef.child(username).child(testId).child("questions");

        setupInterface();
        getQuestions();
    }

    private void setupInterface() {
        questionTitle = (TextView) findViewById(R.id.quizQuestionTitle);
        firstChoiceButton = (Button) findViewById(R.id.firstChoiceButton);
        secondChoiceButton = (Button) findViewById(R.id.secondChoiceButton);
        thirdChoiceButton = (Button) findViewById(R.id.thirdChoiceButton);
        fourthChoiceButton = (Button) findViewById(R.id.fourthChoiceButton);
    }

    private void getQuestions() {
        questions = new ArrayList<Question>();

        userQuestionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Question question = d.getValue(Question.class);
                        questions.add(question);
                    }

                    extractCorrectAnswers();
                    setupFirstQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void extractCorrectAnswers() {
        correctAnswers = new ArrayList<Integer>();
        for (Question q : questions) {
            switch (q.correctAnswer) {
                case "A":
                    correctAnswers.add(0);
                    break;
                case "B":
                    correctAnswers.add(1);
                    break;
                case "C":
                    correctAnswers.add(2);
                    break;
                case "D":
                    correctAnswers.add(3);
                    break;
            }

        }
    }

    private void setupFirstQuestion() {
        if (questions.size() > 0) {
            Question q = questions.get(0);
            questionTitle.setText(q.title);
            firstChoiceButton.setText(q.answerChoices.get(0));
            secondChoiceButton.setText(q.answerChoices.get(1));
            thirdChoiceButton.setText(q.answerChoices.get(2));
            fourthChoiceButton.setText(q.answerChoices.get(3));
        }
    }

    public void firstAnswerClicked(View view) {
        boolean isCorrect = isCorrectAnswer(0);
    }

    public void secondAnswerClicked(View view) {
        boolean isCorrect = isCorrectAnswer(1);
    }

    public void thirdAnswerClicked(View view) {
        boolean isCorrect = isCorrectAnswer(2);
    }

    public void fourthAnswerClicked(View view) {
        boolean isCorrect = isCorrectAnswer(3);
    }

    private boolean isCorrectAnswer(int choice) {
        firstAttemptUsed = true;
        return choice == correctAnswers.get(currentQuestion);
    }

    // Transition to test screen.
    public void goToTestScreen() {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
