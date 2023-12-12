package com.cs407.shopnonstoptylersexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Objects;
import java.io.IOException;

public class SettingsPage extends AppCompatActivity {
    private FirebaseAuth auth;

    Button BSelectImage;

    ImageView IVPreviewImage;

    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspage);

        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        TextView textViewUserName = findViewById(R.id.textViewUserName);
        TextView textViewProfileBio = findViewById(R.id.textViewProfileBio);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonSetDistance = findViewById(R.id.buttonSetDistance);
        Button buttonEditProfile = findViewById(R.id.editProfile);
        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.imageViewUserImage);

        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        textViewUserName.setText(email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference db = database.getReference();
        DatabaseReference uidRef = db.child("users").child(uid);

        uidRef.child("bio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    textViewProfileBio.setText(snapshot.getValue(String.class));
                } else {
                    textViewProfileBio.setText("No Bio :(");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                sharedPreferences.edit().clear().apply();
                auth.signOut();
                Intent intent = new Intent(SettingsPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPage.this);
                builder.setTitle("Edit your bio");

                final EditText input = new EditText(SettingsPage.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bio = input.getText().toString();
                        textViewProfileBio.setText(bio);
                        uidRef.child("bio").setValue(bio);
                        Toast.makeText(SettingsPage.this, "Bio updated successfully", Toast.LENGTH_SHORT).show();
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
                        if (distanceValue.trim().isEmpty()) {
                            Toast.makeText(SettingsPage.this, "Cannot enter an empty value", Toast.LENGTH_SHORT).show();
                        } else if (Double.parseDouble(distanceValue) > 10) {
                            Toast.makeText(SettingsPage.this, "Cannot set a radius greater than 10 miles", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsPage.this, "Distance set successfully", Toast.LENGTH_SHORT).show();
                            uidRef.child("distance").setValue(distanceValue);
                        }
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



    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        IVPreviewImage.setImageBitmap(
                                selectedImageBitmap);
                    }
                }
            });



}
