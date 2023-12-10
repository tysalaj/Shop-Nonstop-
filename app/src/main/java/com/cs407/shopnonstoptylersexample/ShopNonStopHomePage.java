package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShopNonStopHomePage extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopnonstop_homepage);


        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        ImageView shoppingCartIcon = findViewById(R.id.shoppingCartIcon);
        ImageView homePageIcon = findViewById(R.id.homePage);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            startLocationService();
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationService();
        }
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        startForegroundService(serviceIntent);
    }

}
