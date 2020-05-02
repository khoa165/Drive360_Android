package com.example.drive360_android.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.drive360_android.R;
import com.example.drive360_android.auth.LoginActivity;
import com.example.drive360_android.forms.AddClassroomActivity;

public class ClassroomDashboardActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_dashboard);
        sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);
    }

    // Transition to add test screen.
    public void goToAddClassroomScreen(View view) {
        Intent intent = new Intent(this, AddClassroomActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
}
