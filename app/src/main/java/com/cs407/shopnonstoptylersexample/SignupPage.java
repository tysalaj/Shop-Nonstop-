package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupPage extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonSignUpFinal;
    private Button buttonSignUp;
    private Button backToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignUpFinal = findViewById(R.id.buttonSignUpFinal);
        backToLogin = findViewById(R.id.backToLogin);




        buttonSignUpFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                Intent intent = new Intent(SignupPage.this, SettingsPage.class);
                intent.putExtra("text", text);
                intent.putExtra("email", email);
                startActivity(intent);
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
