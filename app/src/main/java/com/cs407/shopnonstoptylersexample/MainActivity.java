package com.cs407.shopnonstoptylersexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!username.trim().isEmpty()) {
            gotoHomePage();
        } else {
            setContentView(R.layout.activity_main);
            auth = FirebaseAuth.getInstance();
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
                        Toast.makeText(MainActivity.this, "Enter a username and a password", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.signInWithEmailAndPassword(currUsername, currPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("username", currUsername).apply();
                                gotoHomePage();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
    public void gotoHomePage() {
        Intent intent = new Intent(this, ShopNonStopHomePage.class);
        startActivity(intent);
    }
}