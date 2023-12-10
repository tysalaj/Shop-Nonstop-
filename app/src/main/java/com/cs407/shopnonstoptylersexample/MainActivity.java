package com.cs407.shopnonstoptylersexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (username != "") {
            gotoHomePage();
        } else {
            setContentView(R.layout.activity_main);
            Button signUp = findViewById(R.id.buttonSignUp);
            Button loginButton = findViewById(R.id.buttonLogin);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText username = findViewById(R.id.editTextUsername);
                    EditText password = findViewById(R.id.editTextPassword);
                    String currUsername = username.getText().toString().trim();
                    String currPassword = password.getText().toString().trim();
                    if (currUsername.isEmpty() || currPassword.isEmpty()) {
                        displayCredentialsToast();
                        return;
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("username", currUsername).apply();
                    gotoHomePage();
                }
            });

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SignupPage.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationService();
        }
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        startForegroundService(serviceIntent);
    }

    public void displayCredentialsToast() {
        Toast.makeText(this, "Enter a username and a password", Toast.LENGTH_SHORT).show();
    }

    public void gotoHomePage() {
        Intent intent = new Intent(this, ShopNonStopHomePage.class);
        startActivity(intent);
        startLocationService();
    }
    private void attemptLogin() {
        String username = "";
        String password = "";

        // Validate credentials (replace this with your own logic)
        if (isValidCredentials(username, password)) {
            // Successful login, navigate to the next screen
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            // Add code to navigate to the next screen, for example:
            // Intent intent = new Intent(this, NextActivity.class);
            // startActivity(intent);
        } else {
            // Invalid credentials, show an error message
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Replace this with your own logic to validate the credentials
        return username.equals("example") && password.equals("password");
    }
}