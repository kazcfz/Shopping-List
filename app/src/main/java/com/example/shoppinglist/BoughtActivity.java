package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.TypeConverter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BoughtActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHandler databaseHandler;
    private List<ShoppingItem> shoppingItemList;
    private Context context;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought);
        getSupportActionBar().setTitle("Items Bought");
        context = this;

        initUI();
        dbToList();
    }

    // Initialise UI and others
    private void initUI(){
        // Bottom Navigation View
        setBottomNavigationView();

        // Floating Action Button
        floatingActionButton = findViewById(R.id.floatingActionButton_Bought);
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
        recyclerView = findViewById(R.id.recyclerView_Bought);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDivider);

        // Database Handler
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

        // Filter bought items
        // Prepare list for deleting
        ArrayList<ShoppingItem> tempDelete = new ArrayList<>();
        shoppingItemList = databaseHandler.readAllItems();
        for (int i=0; i<shoppingItemList.size(); i++){
            if (!shoppingItemList.get(i).isBought()) {
                tempDelete.add(shoppingItemList.get(i));
            }
        }

        // Commence delete (filter)
        for (int i=0; i<tempDelete.size(); i++) {
            shoppingItemList.remove(tempDelete.get(i));
        }
        tempDelete.clear();

        // Sort dates starting from latest
        // Converts string to date format first
        Comparator<ShoppingItem> dateComparator = new Comparator<ShoppingItem>() {
            @Override
            public int compare(ShoppingItem s1, ShoppingItem s2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date s1Date = null;
                Date s2Date = null;
                try {
                    s1Date = sdf.parse(s1.getDateBought());
                    s2Date = sdf.parse(s2.getDateBought());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return s1Date.compareTo(s2Date);
            }
        };
        // Sorts list by Date (descending)
        shoppingItemList.sort(dateComparator.reversed());
        // Sets adapter
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
        bottomNavigationView = findViewById(R.id.bottomNavigationView_Bought);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Urgent List
                if (item.getItemId() == R.id.urgentList) {
                    Intent intent = new Intent();
                    intent.setClass(context, UrgentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
                // Home
                else if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
                return true;
            }
        });
    }
}