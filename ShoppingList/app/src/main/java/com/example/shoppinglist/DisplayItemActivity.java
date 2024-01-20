package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayItemActivity extends AppCompatActivity {

    private TextView textView_name, textView_details, textView_quantity, textView_size;
    private ImageView imageView_urgent;
    private ShoppingItem shoppingItem;
    private Context context;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);
        getSupportActionBar().setTitle(R.string.displayitem);
        databaseHandler = MainActivity.databaseHandler;
        context = this;

        initUI();
        initItem();
    }

    // Refreshes display of item
    @Override
    protected void onStart() {
        super.onStart();
        initItem();
    }

    // Initialise display of an item
    private void initItem(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            // Gets shoppingItem selected and queries read from database
            shoppingItem = (ShoppingItem) bundle.getSerializable("ShoppingItem");
            ShoppingItem shoppingItemDisplay = databaseHandler.readItem(shoppingItem.getId());

            // Set up display for everything
            textView_name.setText(shoppingItemDisplay.getName());
            textView_details.setText(shoppingItemDisplay.getDetails());
            textView_quantity.setText(Integer.toString(shoppingItemDisplay.getQuantity()));
            textView_size.setText(shoppingItemDisplay.getSize());
            if (shoppingItemDisplay.isUrgent())
                imageView_urgent.setImageResource(R.drawable.checked);
            else
                imageView_urgent.setImageResource(R.drawable.unchecked);
        }
    }

    // Initialise findViewById
    private void initUI(){
        textView_name = findViewById(R.id.textView_ShoppingItem_name);
        textView_details = findViewById(R.id.textView_detailText);
        textView_details.setMovementMethod(new ScrollingMovementMethod());
        textView_quantity = findViewById(R.id.textView_quantityText);
        textView_size = findViewById(R.id.textView_sizeText);
        imageView_urgent = findViewById(R.id.imageView_urgentStatus);
    }

    // Inflate menu with Edit button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!shoppingItem.isBought())
            getMenuInflater().inflate(R.menu.menu_displayitem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // If Edit button is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Sends selected shoppingItem and starts new Edit Item activity
        Bundle editBundle = new Bundle();
        editBundle.putSerializable("EditItem", shoppingItem);
        Intent intent = new Intent();
        intent.setClass(context, EditActivity.class);
        intent.putExtras(editBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}