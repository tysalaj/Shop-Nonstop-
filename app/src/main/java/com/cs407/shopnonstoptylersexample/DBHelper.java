package com.cs407.shopnonstoptylersexample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase sqLiteDatabase;

    public DBHelper (SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS items" +
                "(id INTEGER PRIMARY KEY,username TEXT, itemName TEXT)");
    }

    public void saveNotes(String username, String itemName) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO items (username, itemName) VALUES (?, ?)",
                new String[]{username, itemName});
    }

    public void updateItem(String username, String itemName) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO item (usernmae, itemName) VALUES (?, ?)",
                new String[]{username, itemName});
    }

    public void deleteItem(String username, String itemName) {
        createTable();
        String date = "";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT username FROM items WHERE itemName = ?",
                new String[]{itemName});
        if (cursor.moveToNext()) {
            date = cursor.getString(0);
        }
        sqLiteDatabase.execSQL("DELETE FROM notes WHERE username = ? AND itemName = ?",
                new String[]{username, itemName});
        cursor.close();
    }

    public ArrayList<Item> readNotes(String username) {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE username LIKE ?",
                new String[]{"%" + username + "%"});
        int itemNameIndex = c.getColumnIndex("itemName");
        c.moveToFirst();
        ArrayList<Item> itemsList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String itemName = c.getString(itemNameIndex);

            Item item = new Item(username, itemName);
            itemsList.add(item);
            c.moveToNext();
        }
        c.close();
        return itemsList;
    }
}