package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ShopNonStopHomePage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnonstop_homepage);


        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        ImageView shoppingCartIcon = findViewById(R.id.shoppingCartIcon);

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopNonStopHomePage.this, SettingsPage.class);
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
