package com.cs407.shopnonstoptylersexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp = findViewById(R.id.buttonSignUp);

        TextView homePage = findViewById(R.id.buttonLogin);

        String username = "username";

        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("username", "")!= ""){
            Intent noteIntent = new Intent(this, ShopNonStopHomePage.class);
            noteIntent.putExtra("username", sharedPreferences.getString("username", ""));
            startActivity(noteIntent);
        }else{
            setContentView(R.layout.activity_main);
        }

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
}