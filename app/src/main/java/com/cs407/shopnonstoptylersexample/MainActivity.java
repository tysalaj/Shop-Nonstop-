package com.cs407.shopnonstoptylersexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (username != "") {
            gotoHomePage();
        } else {
            setContentView(R.layout.activity_main);
        }

        Button signUp = findViewById(R.id.buttonSignUp);
        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.editTextUsername);
                EditText password = findViewById(R.id.editTextPassword);
                if (username.getText().toString().equals("") || password.getText().toString().equals("")) return;
                SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("username", username.getText().toString()).apply();
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

    public void gotoHomePage() {
        Intent intent = new Intent(this, ShopNonStopHomePage.class);
        startActivity(intent);
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