package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    private EditText name, details;
    private ImageButton inc, dec;
    private TextView quantity;
    private Spinner size;
    private CheckBox urgent;
    private Button addButton;
    private DatabaseHandler databaseHandler;
    private ShoppingItem shoppingItem, shoppingItemDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setTitle("Edit Item");
        databaseHandler = MainActivity.databaseHandler;

        // Initialise findViewById
        name = findViewById(R.id.editText_name_EDIT);
        details = findViewById(R.id.editText_details_EDIT);
        inc = findViewById(R.id.imageButton_increaseQty_EDIT);
        dec = findViewById(R.id.imageButton_decreaseQty_EDIT);
        quantity = findViewById(R.id.textView_quantityTextAdd_EDIT);
        size = findViewById(R.id.spinner_size_EDIT);
        urgent = findViewById(R.id.checkBox_urgent_EDIT);
        addButton = findViewById(R.id.button_update);

        // Gets selected shoppingItem
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            shoppingItem = (ShoppingItem) bundle.getSerializable("EditItem");
            shoppingItemDisplay = databaseHandler.readItem(shoppingItem.getId());

            // Sets the Views with the selected data
            name.setText(shoppingItemDisplay.getName());
            details.setText(shoppingItemDisplay.getDetails());
            quantity.setText(Integer.toString(shoppingItemDisplay.getQuantity()));
            urgent.setChecked(shoppingItemDisplay.isUrgent());
        }

        // Check focus on Name EditText
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Sets an error if empty
                if (!b){
                    if (name.getText().toString().trim().matches(""))
                        name.setError("Name cannot be empty");
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
        // Set spinner with selected items'
        if (bundle != null) {
            int selectionPos = sizeAdapter.getPosition(shoppingItemDisplay.getSize());
            size.setSelection(selectionPos);
        }

        // Add button Clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks for error
                if (name.getText().toString().trim().matches(""))
                    name.setError("Please enter the item to be purchased");
                else {
                    // Sets the shoppingItem with the edited values
                    shoppingItem.setName(name.getText().toString());
                    shoppingItem.setDetails(details.getText().toString());
                    shoppingItem.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    shoppingItem.setSize(size.getSelectedItem().toString());
                    shoppingItem.setUrgent(urgent.isChecked());

                    // Modify in database
                    databaseHandler.modifyItem(shoppingItem);

                    finish();
                }
            }
        });
    }
}