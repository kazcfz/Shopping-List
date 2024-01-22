package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UrgentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHandler databaseHandler;
    private List<ShoppingItem> shoppingItemList;
    private Context context;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent);
        context = this;
        getSupportActionBar().setTitle("Urgent Items");

        initUI();
        dbToList();
    }

    private void initUI(){
        // Bottom Navigation View
        setBottomNavigationView();

        // Floating Action Button
        floatingActionButton = findViewById(R.id.floatingActionButton_Urgent);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, AddItemActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView_Urgent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDivider);

        // Access to database
        databaseHandler = MainActivity.databaseHandler;
    }

    // Refresh list and adapter
    @Override
    protected void onStart() {
        super.onStart();
        dbToList();
    }

    // Get all of Database and put into the ArrayList
    private void dbToList(){
        if (shoppingItemList != null)
            shoppingItemList.clear();

        // Filters urgent items, bought items not included
        ArrayList<ShoppingItem> tempDelete = new ArrayList<>();
        shoppingItemList = databaseHandler.readAllItems();
        for (int i=0; i<shoppingItemList.size(); i++){
                if (!shoppingItemList.get(i).isUrgent() || shoppingItemList.get(i).isBought()) {
                    tempDelete.add(shoppingItemList.get(i));
                }
        }

        // Delete commence (filter)
        for (int i=0; i<tempDelete.size(); i++) {
            shoppingItemList.remove(tempDelete.get(i));
        }
        tempDelete.clear();

        setItemAdapter();
    }

    // Function to set Adapter
    private void setItemAdapter(){
        itemAdapter = new ItemAdapter(shoppingItemList, databaseHandler);
        recyclerView.setAdapter(itemAdapter);
    }

    // Bottom Navigation View
    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView_Urgent);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Home
                if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
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