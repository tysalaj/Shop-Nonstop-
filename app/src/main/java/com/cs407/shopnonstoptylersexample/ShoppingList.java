package com.cs407.shopnonstoptylersexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Random;

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

        ArrayList<String> itemKeys = new ArrayList<>();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference db = database.getReference();
        DatabaseReference uidRef = db.child("users").child(uid).child("items");


        ListView currentShoppingList = (ListView) findViewById(R.id.shoppingListView);
        currentShoppingList.setClickable(true);
        currentShoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int distance = new Random().nextInt(6);
                AlertDialog.Builder dialogBox = new AlertDialog.Builder(ShoppingList.this);
                dialogBox.setTitle(currentShoppingList.getItemAtPosition(position).toString());
                dialogBox.setMessage("Found this grocery item within " + distance + " miles of you!");
                dialogBox.setPositiveButton("Shop Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ShoppingList.this, ShopNonStopHomePage.class);
                        startActivity(intent);
                    }
                });
                dialogBox.setNegativeButton("Delete Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uidRef.child(itemKeys.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ShoppingList.this, "Removed Item!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialogBox.show();
            }
        });

        ListView mostPopularShoppingList = (ListView) findViewById(R.id.mostPopularItemsList);
        mostPopularShoppingList.setClickable(true);
        mostPopularShoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int randomNumber = new Random().nextInt(30) + 10;
                AlertDialog.Builder dialogBox = new AlertDialog.Builder(ShoppingList.this);
                String itemName = mostPopularShoppingList.getItemAtPosition(position).toString();
                dialogBox.setTitle(itemName);
                dialogBox.setMessage("Shopped for this item " + randomNumber + " times!");
                dialogBox.setPositiveButton("Minimize", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBox.setNegativeButton("Delete Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBox.show();
            }
        });

        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemKeys.clear();
                ArrayList<String> items = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    items.add(dataSnapshot.getValue(String.class));
                    itemKeys.add(dataSnapshot.getKey());
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
                String itemName = item.getText().toString().trim().toLowerCase();
                if (itemName.isEmpty()) {
                    Toast.makeText(ShoppingList.this, "Need to enter an item name!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ShoppingList.this, "Added new item!", Toast.LENGTH_SHORT).show();
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
