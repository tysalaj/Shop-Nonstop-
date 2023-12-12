package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonSignUpFinal = findViewById(R.id.buttonSignUpFinal);
        Button backToLogin = findViewById(R.id.backToLogin);

        buttonSignUpFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String conformPassword = editTextConfirmPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(SignupPage.this, "Need to enter an email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(SignupPage.this, "Need to enter an password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(conformPassword)) {
                    Toast.makeText(SignupPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupPage.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupPage.this, MainActivity.class));
                            } else {
                                Toast.makeText(SignupPage.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }




}
