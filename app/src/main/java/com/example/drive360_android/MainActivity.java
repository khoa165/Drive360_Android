package com.example.drive360_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.drive360_android.auth.LoginActivity;
import com.example.drive360_android.forms.FeedbackActivity;
import com.example.drive360_android.pages.AdminDashboardActivity;
import com.example.drive360_android.pages.ClassroomDashboardActivity;
import com.example.drive360_android.pages.LearnActivity;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAuthentication();

        // Set up weekly notifications
        alarm();
    }

    // Check if user is authenticate/login.
    private void checkAuthentication () {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        // Check if user is authenticated.
        boolean isAuthenticated = sharedPreferences.getBoolean("isAuthenticated", false);
        if (isAuthenticated) {
            setContentView(R.layout.activity_main);
        } else {
            goToLoginScreen();
        }
    }

    // Redirect the user to login first.
    public void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Transition to admin dashboard screen.
    public void goToAdminDashboardScreen() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
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

            sharedPreferences.edit().putBoolean("isAuthenticated", false).apply();
            sharedPreferences.edit().putBoolean("isAdmin", false).apply();
            sharedPreferences.edit().putBoolean("isInstructor", false).apply();
            sharedPreferences.edit().remove("username").apply();
            sharedPreferences.edit().remove("testId").apply();
            sharedPreferences.edit().remove("questionId").apply();

            // Redirect the user to login screen.
            goToLoginScreen();
            Toast.makeText(MainActivity.this, "Sign out successful!", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.admin_dashboard) {
            goToAdminDashboardScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reminds user every Monday at 9am to learn
     */
    public void alarm() {
        // Use calendar and alarm manager to set up recurring notifications
        Calendar alarmTime = Calendar.getInstance();

        // Set up time to remind user
        alarmTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        alarmTime.set(Calendar.HOUR_OF_DAY, 9);
        alarmTime.set(Calendar.MINUTE, 0);
        alarmTime.set(Calendar.SECOND, 0);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        // Check if it in the future
        if (now.getTimeInMillis() <  alarmTime.getTimeInMillis()) {
            // Nothing to do - time of alarm in the future
        } else {
            int dayDiffBetweenClosestMonday = (7 + now.get(Calendar.DAY_OF_WEEK) - alarmTime.get(Calendar.DAY_OF_WEEK)) % 7;

            if (dayDiffBetweenClosestMonday == 0) {
                // Today is Monday, but current time after 9am, so schedule for the next Monday
                dayDiffBetweenClosestMonday = 7;
            }

            alarmTime.add(Calendar.DAY_OF_MONTH, dayDiffBetweenClosestMonday);
        }

        // This id is used to set multiple alarms
        final int _id = (int) System.currentTimeMillis();

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Makes sure alarm will fire and wake up screen (1st arg)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pendingIntent);
    }

    public void btnLoadUnity(View v) {
        // Intent intent = new Intent(this, MainUnityActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // startActivityForResult(intent, 1);
    }

    // Transition to feedback screen.
    public void goToFeedbackScreen(View view) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    // Transition to classroom dashboard screen.
    public void goToClassroomDashboardScreen(View view) {
        Intent intent = new Intent(this, ClassroomDashboardActivity.class);
        startActivity(intent);
    }

    // Transition to learn screen.
    public void goToLearnScreen(View view) {
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
    }
}
