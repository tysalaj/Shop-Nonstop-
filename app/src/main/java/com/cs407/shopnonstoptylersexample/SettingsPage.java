package com.cs407.shopnonstoptylersexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsPage extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspage);

        auth = FirebaseAuth.getInstance();
        ImageView imageViewUserImage = findViewById(R.id.imageViewUserImage);
        TextView textViewUserName = findViewById(R.id.textViewUserName);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        EditText editTextsetDistance = findViewById(R.id.editTextSetDistance);




        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        textViewUserName.setText(email);

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
                String distanceValue = editTextsetDistance.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
                sharedPreferences.edit().putString("distance", distanceValue).apply();
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
