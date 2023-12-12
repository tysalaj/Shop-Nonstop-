package com.cs407.shopnonstoptylersexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button update = findViewById(R.id.updatePassword1);
        Button back = findViewById(R.id.backToSettings1);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangePassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassword.this, SettingsPage.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, SettingsPage.class);
                startActivity(intent);
            }
        });

    }
}