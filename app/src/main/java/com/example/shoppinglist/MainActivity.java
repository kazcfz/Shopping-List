package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ShoppingItem> shoppingItemList;
    private Context context;
    private FloatingActionButton floatingActionButton;
    public static DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initUI();   // initialize UI
        //databaseHandler = new DatabaseHandler(this, "PublishedDB1", null, 1); // Create real database
        demoPopulateDB();   // Populate database for demo purposes
        dbToList(); // Puts data from database to local List and sets adapter
    }

    // Calls Refresh list and adapter
    @Override
    protected void onStart() {
        super.onStart();
        dbToList();
    }

    // Initialize UI
    private void initUI(){
        // Bottom Navigation View
        setBottomNavigationView();

        // Floating Action Button
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, AddItemActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });

        // Recycler View
        recyclerView = findViewById(R.id.recyclerView_Main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDivider);
    }

    // Get all of Database and put into the ArrayList
    private void dbToList(){
        if (shoppingItemList != null)
            shoppingItemList.clear();

        // Filters all but bought items
        // Another list to delete ShoppingItem objects from
        ArrayList<ShoppingItem> tempDelete = new ArrayList<>();
        shoppingItemList = databaseHandler.readAllItems();
        for (int i=0; i<shoppingItemList.size(); i++){
            if (shoppingItemList.get(i).isBought())
                tempDelete.add(shoppingItemList.get(i));
        }

        // Commence delete (filter)
        for (int i=0; i<tempDelete.size(); i++) {
            shoppingItemList.remove(tempDelete.get(i));
        }
        tempDelete.clear();

        // Sorts the list by Name (ascending)
        shoppingItemList.sort(Comparator.comparing(ShoppingItem::getName, String.CASE_INSENSITIVE_ORDER));
        // Sets adapter
        setItemAdapter();
    }

    // Function to set Adapter
    private void setItemAdapter(){
        itemAdapter = new ItemAdapter(shoppingItemList, databaseHandler);
        recyclerView.setAdapter(itemAdapter);
    }

    // Demo for items added to Database
    private void demoPopulateDB(){
        databaseHandler = new DatabaseHandler(this, null, null, 1);

        databaseHandler.createItem(new ShoppingItem("Cereal", "Detail1", 1, "Default", false, false));
        databaseHandler.createItem(new ShoppingItem("Burger", "Detail2", 2, "Medium", true, false));
        databaseHandler.createItem(new ShoppingItem("BigSmoke", "Detail3", 3, "Large", true, false));
        databaseHandler.createItem(new ShoppingItem("TestBought", "Detail4", 4, "Large", false, true, "1 Jan 2021"));
        databaseHandler.createItem(new ShoppingItem("TestBought2", "Detail5", 5, "Large", false, true, "2 Feb 2021"));
    }

    // Bottom Navigation View
    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Urgent List
                if (item.getItemId() == R.id.urgentList) {
                    Intent intent = new Intent();
                    intent.setClass(context, UrgentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
                // Completed List
                else if (item.getItemId() == R.id.completedList) {
                    Intent intent = new Intent();
                    intent.setClass(context, BoughtActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
                return true;
            }
        });
    }
}