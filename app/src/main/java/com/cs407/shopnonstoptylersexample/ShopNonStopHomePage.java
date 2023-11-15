package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.SupportMapFragment;

public class ShopNonStopHomePage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnonstop_homepage);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);

        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        ImageView shoppingCartIcon = findViewById(R.id.shoppingCartIcon);

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopNonStopHomePage.this, settingsPage.class);
                startActivity(intent);
            }
        });

        shoppingCartIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ShopNonStopHomePage.this, ShoppingList.class);
                startActivity(intent);
            }
        });
    }
}
