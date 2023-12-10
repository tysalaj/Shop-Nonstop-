package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsPage extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspage);

        auth = FirebaseAuth.getInstance();
        ImageView imageViewUserImage = findViewById(R.id.imageViewUserImage);
        TextView textViewUserName = findViewById(R.id.textViewUserName);
        TextView textViewUserEmail = findViewById(R.id.textViewUserEmail);
        Button buttonSetLocation = findViewById(R.id.buttonSetLocation);
        Button buttonSetDistance = findViewById(R.id.buttonSetDistance);
        Button buttonLogout = findViewById(R.id.buttonLogout);


        String userName = getIntent().getStringExtra("text");
        String userEmail = getIntent().getStringExtra("email");

        if (userName != null && userEmail != null) {
            textViewUserName.setText(userName);
            textViewUserEmail.setText(userEmail);
        }

        ImageView shoppingCartIcon = findViewById(R.id.shoppingCartIcon);
        ImageView homePageIcon = findViewById(R.id.homePage);


        shoppingCartIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SettingsPage.this, ShoppingList.class);
                startActivity(intent);
            }
        });

        homePageIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SettingsPage.this, ShopNonStopHomePage.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(SettingsPage.this, LocationService.class));
                SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                auth.signOut();
                Intent intent = new Intent(SettingsPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
