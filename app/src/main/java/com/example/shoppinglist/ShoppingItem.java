package com.example.shoppinglist;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingItem implements Serializable {
    private int id;
    private String name;
    private String details;
    private int quantity;
    private String size;
    private boolean urgent;
    private boolean bought;
    private String dateBought;

    // Empty Constructor
    public ShoppingItem() {
    }

    // Constructor that takes some parameters
    public ShoppingItem(String name, String details, int quantity, String size, boolean urgent, boolean bought) {
        this.name = name;
        this.details = details;
        this.quantity = quantity;
        this.size = size;
        this.urgent = urgent;
        this.bought = bought;
    }

    // Constructor that takes all parameters except ID
    public ShoppingItem(String name, String details, int quantity, String size, boolean urgent, boolean bought, String dateBought) {
        this.name = name;
        this.details = details;
        this.quantity = quantity;
        this.size = size;
        this.urgent = urgent;
        this.bought = bought;
        this.dateBought = dateBought;
    }

    // Constructor that takes all parameters
    public ShoppingItem(int id, String name, String details, int quantity, String size, boolean urgent, boolean bought, String dateBought) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.quantity = quantity;
        this.size = size;
        this.urgent = urgent;
        this.bought = bought;
        this.dateBought = dateBought;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public boolean isBought() {
        return bought;
    }

    public String getDateBought() {
        return dateBought;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public void setDateBought(String dateBought) {
        this.dateBought = dateBought;
    }
}
