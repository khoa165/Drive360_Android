package com.example.drive360_android.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drive360_android.R;
import com.example.drive360_android.auth.LoginActivity;
import com.example.drive360_android.forms.AddTestActivity;
import com.example.drive360_android.models.Test;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDB;
    private DatabaseReference rootRef;
    private DatabaseReference adminTestRef;
    private DatabaseReference userTestRef;
    private ArrayList<String> tests;
    private ArrayList<String> firebaseIds;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Initialize database, root and admin test references.
        firebaseDB = FirebaseDatabase.getInstance();
        rootRef = firebaseDB.getReference();
        adminTestRef = rootRef.child("admin_tests");

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        // Get current user's username.
        String username = sharedPreferences.getString("username", "");
        // Initialize user test references.
        userTestRef = rootRef.child("user_tests").child(username);

        setupListView();
        setupTestItemListener();
    }

    private void setupListView() {
        tests = new ArrayList<String>();
        firebaseIds = new ArrayList<String>();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tests);
        listView = findViewById(R.id.testList);

        adminTestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d1 : dataSnapshot.getChildren()) {
                        // tests.add(d1.getKey());
                    }

                    userTestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for(DataSnapshot d2 : dataSnapshot.getChildren()) {
                                    Test test = d2.getValue(Test.class);
                                    tests.add(test.name);
                                    firebaseIds.add(d2.getKey());
                                }

                                listView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setupTestItemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initialize intent to take user to TestQuestionsActivity.
                Intent intent = new Intent(getApplicationContext(), TestQuestionsActivity.class);

                String firebaseId = firebaseIds.get(position);
                // Add the position of the item that was clicked on as "noteid".
                intent.putExtra("testId", firebaseId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        // Get username and set text of menu item to welcome user.
        String username = sharedPreferences.getString("username", "");
        if (username != null && !username.equals("")) {
            MenuItem item = menu.findItem(R.id.welcome);
            item.setTitle("Welcome " + username);
        }

        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
        if (!isAdmin) {
            menu.findItem(R.id.admin_dashboard).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

            // Set isAuthenticated to false and remove username form sharedPreferences.
            sharedPreferences.edit().putBoolean("isAuthenticated", false).apply();
            sharedPreferences.edit().remove("username").apply();

            // Redirect the user to login screen.
            goToLoginScreen();
            return true;
        } else if (item.getItemId() == R.id.admin_dashboard) {
            goToAdminDashboardScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    // Transition to login screen.
    private void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Transition to admin dashboard screen.
    private void goToAdminDashboardScreen() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }

    // Transition to add test screen.
    public void goToAddTestScreen(View view) {
        Intent intent = new Intent(this, AddTestActivity.class);
        startActivity(intent);
    }
}
