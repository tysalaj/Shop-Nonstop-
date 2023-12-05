package com.cs407.shopnonstoptylersexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {
    static ArrayList<Item> items;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglistpage);

        ImageView settings = findViewById(R.id.settingsIcon);
        ImageView addIcon = findViewById(R.id.addIcon);
        SharedPreferences sharedPreferences = getSharedPreferences("com.cs407.shopnonstoptylersexample", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String upUsername = username.substring(0, 1).toUpperCase() + username.substring(1);
        TextView textView = findViewById(R.id.subtitleTextView);
        textView.setText(upUsername + "'s Shopping List");

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, SettingsPage.class);
                startActivity(intent);
            }
        });

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("items", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        items = dbHelper.readNotes(username);
        ArrayList<String> shoppingItems = new ArrayList<>();

        for (Item item : items) {
            shoppingItems.add(item.getItemName());
        }

        ArrayAdapter<String> shoppingListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItems);
        ListView shoppingListView = findViewById(R.id.shoppingListViewPopular);
        shoppingListView.setAdapter(shoppingListAdapter);

        ListView shoppingListViewPopular = findViewById(R.id.shoppingListView);
        ArrayList<String> shoppingItemsPop = new ArrayList<>();
        ArrayAdapter<String> adapterPop = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItemsPop);
        shoppingListViewPopular.setAdapter(adapterPop);

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText item = findViewById(R.id.addItem);
                String itemName = item.getText().toString().trim();
                ArrayList<Item> currShoppingItems = dbHelper.readNotes(username);
                if (!itemName.equals("")) {
                    for (Item currItem : currShoppingItems) {
                        if (currItem.getItemName().toLowerCase().equals(itemName.toLowerCase())) {
                            displayItemExistsToast();
                            return;
                        }
                    }
                    shoppingListAdapter.add(itemName);
                    dbHelper.saveNotes(username, itemName);
                } else {
                   displayEmptyItemToast();
                }
            }
        });
    }

    public void displayItemExistsToast() {
        Toast.makeText(this, "Item is already in your list!", Toast.LENGTH_SHORT).show();
    }

    public void displayEmptyItemToast() {
        Toast.makeText(this, "Need to enter an item name!", Toast.LENGTH_SHORT).show();
    }

}
