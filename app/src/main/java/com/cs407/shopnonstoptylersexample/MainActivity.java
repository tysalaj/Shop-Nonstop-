package com.cs407.shopnonstoptylersexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp = findViewById(R.id.buttonSignUp);
        Button homePage = findViewById(R.id.buttonLogin);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);


        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShopNonStopHomePage.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, signupPage.class);
                startActivity(intent);
            }
        });
    }


    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

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