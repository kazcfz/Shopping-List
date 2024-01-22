package com.example.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.annotation.Size;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ItemDB";
    private static final String TABLE_NAME = "ItemTable";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DETAILS = "details";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_SIZE = "size";
    private static final String COL_URGENT = "urgent";
    private static final String COL_BOUGHT = "bought";
    private static final String COL_DATEBOUGHT = "datebought";

    // Constructor for DatabaseHandler
    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Creating the database with columns
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_QUANTITY + " INTEGER, " +
                COL_SIZE + " TEXT, " +
                COL_URGENT + " INTEGER, " +
                COL_BOUGHT + " INTEGER, " +
                COL_DATEBOUGHT + " DATETIME)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        System.out.println("========== TABLE CREATED!!! ==========");
    }

    // onUpgrade function
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
        System.out.println("========== TABLE DROPPED!!! ==========");
    }

    // Create an item into database
    public long createItem(ShoppingItem shoppingItem){
        SQLiteDatabase itemDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int urgent;
        int bought;

        // Puts entered data into ContentValues
        contentValues.put(COL_NAME, shoppingItem.getName());
        contentValues.put(COL_DETAILS, shoppingItem.getDetails());
        contentValues.put(COL_QUANTITY, shoppingItem.getQuantity());
        contentValues.put(COL_SIZE, shoppingItem.getSize());
        if (shoppingItem.isUrgent())
            urgent = 1;
        else
            urgent = 0;
        contentValues.put(COL_URGENT, urgent);
        if (shoppingItem.isBought())
            bought = 1;
        else
            bought = 0;
        contentValues.put(COL_BOUGHT, bought);

        if (shoppingItem.getDateBought() == null)
            contentValues.put(COL_DATEBOUGHT, "");
        else
            contentValues.put(COL_DATEBOUGHT, shoppingItem.getDateBought());

        // Finally inserts the data
        return itemDB.insert(TABLE_NAME, null, contentValues);
    }

    // Reads an item from the database based on itemID
    public ShoppingItem readItem(int itemID){
        ShoppingItem shoppingItem = null;
        SQLiteDatabase itemDB = this.getReadableDatabase();
        // Query database with itemID
        Cursor cursor = itemDB.query(TABLE_NAME, new String[]{COL_ID, COL_NAME, COL_DETAILS, COL_QUANTITY, COL_SIZE, COL_URGENT, COL_BOUGHT, COL_DATEBOUGHT}, COL_ID + "=?", new String[]{String.valueOf(itemID)}, null, null, null, null);

        // Reads the item based on itemID
        System.out.println("========== READING!!!: " + itemID + " ==========");
        if (cursor != null && cursor.moveToFirst()){
            cursor.moveToFirst();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String details = cursor.getString(2);
            int quantity = cursor.getInt(3);
            String size = cursor.getString(4);
            int urgentINT = cursor.getInt(5);
            boolean urgent;
            int boughtINT = cursor.getInt(6);
            boolean bought;
            String datebought = cursor.getString(7);
            if (urgentINT == 1)
                urgent = true;
            else
                urgent = false;

            if (boughtINT == 1)
                bought = true;
            else
                bought = false;

            // Creates the shoppingItem to return
            shoppingItem = new ShoppingItem(id, name, details, quantity, size
                    , urgent, bought, datebought);
        }
        // Returns the item back to caller
        return shoppingItem;
    }

    // Reads all data from database
    public List<ShoppingItem> readAllItems(){
        List<ShoppingItem> itemList = new ArrayList<ShoppingItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase itemDB = this.getReadableDatabase();
        Cursor cursor = itemDB.rawQuery(selectQuery, null);

        // Reads each data from the first row to the last
        if (cursor.moveToFirst())
        {
            do {
                // Initialise and fill shoppingItem
                ShoppingItem shoppingItem = new ShoppingItem();
                shoppingItem.setId(cursor.getInt(0));
                shoppingItem.setName(cursor.getString(1));
                shoppingItem.setDetails(cursor.getString(2));
                shoppingItem.setQuantity(cursor.getInt(3));
                shoppingItem.setSize(cursor.getString(4));
                int urgentINT = cursor.getInt(5);
                boolean urgent;
                if (urgentINT == 1)
                    urgent = true;
                else
                    urgent = false;
                shoppingItem.setUrgent(urgent);
                int boughtINT = cursor.getInt(6);
                boolean bought;
                if (boughtINT == 1)
                    bought = true;
                else
                    bought = false;
                shoppingItem.setBought(bought);
                shoppingItem.setDateBought(cursor.getString(7));

                // Adds each item into an itemList
                itemList.add(shoppingItem);
            } while (cursor.moveToNext());
        }
        // Returns the itemList
        return itemList;
    }

    // Modify/Change an item
    public int modifyItem(ShoppingItem shoppingItem){
        SQLiteDatabase itemDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int urgent;
        int bought;

        // Gets the whole shoppingItem
        contentValues.put(COL_NAME, shoppingItem.getName());
        contentValues.put(COL_DETAILS, shoppingItem.getDetails());
        contentValues.put(COL_QUANTITY, shoppingItem.getQuantity());
        contentValues.put(COL_SIZE, shoppingItem.getSize());
        if (shoppingItem.isUrgent())
            urgent = 1;
        else
            urgent = 0;
        contentValues.put(COL_URGENT, urgent);
        if (shoppingItem.isBought())
            bought = 1;
        else
            bought = 0;
        contentValues.put(COL_BOUGHT, bought);
        contentValues.put(COL_DATEBOUGHT, "");

        // Updates the matching itemID data with the set values
        return itemDB.update(TABLE_NAME, contentValues, COL_ID + "=?", new String[]{String.valueOf(shoppingItem.getId())});
    }

    // After user bought an item
    // Called by the Switch
    public int boughtItem(ShoppingItem shoppingItem){
        SQLiteDatabase itemDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int urgent;
        int bought;

        // Gets the whole shoppingItem
        contentValues.put(COL_NAME, shoppingItem.getName());
        contentValues.put(COL_DETAILS, shoppingItem.getDetails());
        contentValues.put(COL_QUANTITY, shoppingItem.getQuantity());
        contentValues.put(COL_SIZE, shoppingItem.getSize());
        if (shoppingItem.isUrgent())
            urgent = 1;
        else
            urgent = 0;
        contentValues.put(COL_URGENT, urgent);
        if (shoppingItem.isBought())
            bought = 1;
        else
            bought = 0;
        contentValues.put(COL_BOUGHT, bought);

        // Adds the current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String currentDateSTR = sdf.format(new Date());
        contentValues.put(COL_DATEBOUGHT, currentDateSTR);

        // Updates the shoppingItem matching the itemID
        return itemDB.update(TABLE_NAME, contentValues, COL_ID + "=?", new String[]{String.valueOf(shoppingItem.getId())});
    }

    // Removal of a ShoppingItem
    public int deleteItem(ShoppingItem shoppingItem){
        SQLiteDatabase itemDB = this.getWritableDatabase();
        // Deletes by matching itemID
        return itemDB.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(shoppingItem.getId())});
    }
}
