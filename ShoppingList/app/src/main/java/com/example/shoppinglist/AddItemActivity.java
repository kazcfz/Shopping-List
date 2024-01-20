package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {

    private EditText name, details;
    private ImageButton inc, dec;
    private TextView quantity;
    private Spinner size;
    private CheckBox urgent;
    private Button addButton;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setTitle("Add Item");

        // Initialise everything
        databaseHandler = MainActivity.databaseHandler;
        name = findViewById(R.id.editText_name);
        details = findViewById(R.id.editText_details);
        inc = findViewById(R.id.imageButton_increaseQty);
        dec = findViewById(R.id.imageButton_decreaseQty);
        quantity = findViewById(R.id.textView_quantityTextAdd);
        size = findViewById(R.id.spinner_size);
        urgent = findViewById(R.id.checkBox_urgent);
        addButton = findViewById(R.id.button_add);

        name.requestFocus();    // Focus on Name EditText

        // If focus is changed for Name
        // Checks condition for Error
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    if (name.getText().toString().trim().matches(""))
                        name.setError("Please enter the item to be purchased");
                }
            }
        });

        // Decrease Quantity click listener
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(quantity.getText().toString());
                if (qty != 1){
                    qty--;
                    quantity.setText(String.valueOf(qty));
                }
            }
        });

        // Increase Quantity click listener
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(quantity.getText().toString());
                qty++;
                quantity.setText(String.valueOf(qty));
            }
        });

        // Size spinner adapter
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.size_choice, android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(sizeAdapter);

        // Add Button click listener
        // Error validation before confirming Add Item
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().matches(""))
                    name.setError("Please enter the item to be purchased");
                else {
                    // Fill into variables
                    String nameSTR = name.getText().toString();
                    String detailsSTR = details.getText().toString();
                    int quantityINT = Integer.parseInt(quantity.getText().toString());
                    String sizeSTR = size.getSelectedItem().toString();
                    boolean urgentBOOL = urgent.isChecked();

                    // Adds Item
                    ShoppingItem shoppingItem = new ShoppingItem(nameSTR, detailsSTR, quantityINT, sizeSTR, urgentBOOL, false);
                    databaseHandler.createItem(shoppingItem);

                    finish();
                }
            }
        });
    }
}