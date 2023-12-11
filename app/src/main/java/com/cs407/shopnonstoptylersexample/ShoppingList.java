package com.cs407.shopnonstoptylersexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingList extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglistpage);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ImageView settings = findViewById(R.id.settingsIcon);
        ImageView homePageIcon = findViewById(R.id.homePage);
        ImageView addIcon = findViewById(R.id.addIcon);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, SettingsPage.class);
                startActivity(intent);
            }
        });

        homePageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, ShopNonStopHomePage.class);
                startActivity(intent);
            }
        });


        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Log.i("INFO", uid);
        DatabaseReference db = database.getReference();
        DatabaseReference uidRef = db.child("users").child(uid).child("items");

        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> items = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    items.add(dataSnapshot.getValue(String.class));
                }
                ArrayAdapter<String> shoppingListAdapter = new ArrayAdapter<>(ShoppingList.this, android.R.layout.simple_list_item_1, items);
                ListView shoppingListView = findViewById(R.id.shoppingListView);
                shoppingListView.setAdapter(shoppingListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText item = findViewById(R.id.addItem);
                String itemName = item.getText().toString().trim();
                if (itemName.isEmpty()) {
                    Toast.makeText(ShoppingList.this, "Need to enter an item name", Toast.LENGTH_SHORT).show();
                    return;
                }
                uidRef.orderByValue().equalTo(itemName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(ShoppingList.this, "Item already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            String itemId = uidRef.push().getKey();
                            if (itemId != null) {
                                uidRef.child(itemId).setValue(itemName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ShoppingList.this, "Added New Item", Toast.LENGTH_SHORT).show();
                                        item.setText("");
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
