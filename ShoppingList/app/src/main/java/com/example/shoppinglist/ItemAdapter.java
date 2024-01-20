package com.example.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.example.shoppinglist.MainActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ShoppingItem> shoppingItemArrayList;
    private Context context;
    private AlertDialog.Builder builder;
    private final DatabaseHandler databaseHandler;

    // Constructor + Access to database
    public ItemAdapter(List<ShoppingItem> shoppingItemArrayList, DatabaseHandler databaseHandler) {
        this.shoppingItemArrayList = shoppingItemArrayList;
        this.databaseHandler = databaseHandler;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerItem layout for RecyclerViews
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItemArrayList.get(position);

        // Sets the holders
        holder.textView_name.setText(shoppingItem.getName());
        holder.textView_quantity.setText(Integer.toString(shoppingItem.getQuantity()));
        holder.textView_size.setText(shoppingItem.getSize());
        if (shoppingItem.isUrgent())
            holder.imageView_urgent.setImageResource(R.drawable.urgent);
        else if (!shoppingItem.isUrgent())
            holder.imageView_urgent.setImageResource(R.drawable.buy);

        // Items bought have slightly different visuals
        if (shoppingItem.isBought()) {
            holder.imageView_urgent.setImageResource(R.drawable.bought);
            holder.bought.setVisibility(View.GONE);
            holder.textView_date.setVisibility(View.VISIBLE);
            holder.textView_dateText.setVisibility(View.VISIBLE);
            holder.textView_dateText.setText(shoppingItem.getDateBought());
        }
    }

    @Override
    public int getItemCount() {
        return shoppingItemArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView_name, textView_quantity, textView_size, textView_date, textView_dateText;
        public ImageView imageView_urgent;
        public Switch bought;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialise findViewById
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_quantity = itemView.findViewById(R.id.textView_quantity);
            textView_size = itemView.findViewById(R.id.textView_size);
            textView_date = itemView.findViewById(R.id.textView_DateBought);
            textView_dateText = itemView.findViewById(R.id.textView_dateBoughtText);
            imageView_urgent = itemView.findViewById(R.id.imageView_urgent);
            bought = itemView.findViewById(R.id.switch_Bought);

            // Sets click listener for each item
            itemView.setOnClickListener(this);

            // If activity is anything but BoughtActivity (Bought Items)
            if (!(context instanceof BoughtActivity)) {
                // Sets long click listener for each item
                itemView.setOnLongClickListener(this);
                // Check for changes to the Switch button
                bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        // If Switch is checked
                        if (compoundButton.isChecked()) {
                            int itemPos = getAdapterPosition();
                            ShoppingItem boughtItem = shoppingItemArrayList.get(itemPos);
                            // Sets the item to bought
                            boughtItem.setBought(true);
                            // Update 'bought' in database
                            databaseHandler.boughtItem(boughtItem);
                            // Removes from local list
                            shoppingItemArrayList.remove(itemPos);
                            // Notify the removal/change
                            notifyItemRemoved(itemPos);
                        }
                    }
                });
            }
        }

        // If an item is clicked / short-press / selected
        @Override
        public void onClick(View view) {
            // Displays the selected item in Display Activity
            int itemPos = getAdapterPosition();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ShoppingItem", shoppingItemArrayList.get(itemPos));
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(context, DisplayItemActivity.class);
            context.startActivity(intent);
        }

        // If an item is long clicked
        @Override
        public boolean onLongClick(View view) {
            // Brings up confirmation dialog to Delete Item
            int itemPos = getAdapterPosition();
            builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete " + shoppingItemArrayList.get(itemPos).getName() + " from the list?");
            builder.setCancelable(false);

            // If user confirms Delete
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Deletes the selected item
                    databaseHandler.deleteItem(shoppingItemArrayList.get(itemPos));
                    shoppingItemArrayList.remove(shoppingItemArrayList.get(itemPos));
                    System.out.println("========== DELETED!!!: " + itemPos + " ==========");
                    // Notify the removal
                    notifyItemRemoved(itemPos);
                }
            });

            // If user cancels the Deletion
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Cancels dialog
                    dialogInterface.cancel();
                }
            });

            // Dialog box initialisation
            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle(context.getString(R.string.delete_item));
            alertDialog.setIcon(R.drawable.warning_android);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            alertDialog.show();

            return true;
        }
    }
}
