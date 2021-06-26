package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.R;

public class AddSaleAdapter extends RecyclerView.Adapter<AddSaleViewHolder> {

//    private ArrayList<AddSaleItem> items;
    private  ArrayList<Item> items;
    private AddSaleViewHolder viewHolder;
    private Cart runningCart;
    Context context;

//    public AddSaleAdapter(Context context, ArrayList<AddSaleItem> items) {
//        this.items = items;
//        this.context = context;
//    }
    public AddSaleAdapter(Context context, Cart cart)
    {
        this.context = context;

        runningCart = cart;
        items = cart.getItemList();
    }

    @NonNull
    @NotNull
    @Override
    public AddSaleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_sale_item, parent, false);

        viewHolder = new AddSaleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddSaleViewHolder holder, int position) {

//        AddSaleItem item = items.get(position);
//
//        holder.setItemNameText(item.getItemName());
//        holder.setItemQuantityText(item.getItemQuantityText());
//        holder.setItemTotalPriceText(item.getItemTotalPriceText());
//        holder.setItemNumberText(position+1);
        Item item = items.get(position);

        holder.setItemNameText(item.getName());
        holder.setItemQuantityText(String.valueOf(item.getSoldQuantity()));
        holder.setItemTotalPriceText(String.valueOf(item.getTotalPrice()));
        holder.setItemNumberText(position+1);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(ArrayList<Item> items)
    {

        this.items = items;
        notifyDataSetChanged();
    }
    public void addItem(Item item)
    {
        runningCart.addItem(item);
        notifyDataSetChanged();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
