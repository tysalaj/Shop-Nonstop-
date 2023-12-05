package com.cs407.shopnonstoptylersexample;

public class Item {
    private String itemName;
    private String username;

    public Item(String username, String itemName) {
        this.username = username;
        this.itemName = itemName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getUsername() {
        return this.username;
    }

}
