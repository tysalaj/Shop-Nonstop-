package com.cs407.shopnonstoptylersexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglistpage);

        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, settingsPage.class);
                startActivity(intent);
            }
        });

        ListView shoppingListView = findViewById(R.id.shoppingListView);
        ArrayList<String> shoppingItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItems);
        shoppingListView.setAdapter(adapter);

        ListView shoppingListViewPopular = findViewById(R.id.shoppingListViewPopular);
        ArrayList<String> shoppingItemsPop = new ArrayList<>();
        ArrayAdapter<String> adapterPop = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItemsPop);
        shoppingListViewPopular.setAdapter(adapterPop);
    }

}
