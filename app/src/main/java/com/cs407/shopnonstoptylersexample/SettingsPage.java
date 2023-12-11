package com.cs407.shopnonstoptylersexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        Button buttonSetDistance = findViewById(R.id.buttonSetDistance);




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

        buttonSetDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPage.this);
                builder.setTitle("Furthest Distance");

                final EditText input = new EditText(SettingsPage.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String distanceValue = input.getText().toString();
                        if (distanceValue.trim().equals("")) {
                            Toast.makeText(SettingsPage.this, "Cannot enter an empty value", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (Double.parseDouble(distanceValue) > 10) {
                            Toast.makeText(SettingsPage.this, "Cannot set a radius greater than 10 miles", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
                        sharedPreferences.edit().putString("distance", distanceValue).apply();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
    }


}
