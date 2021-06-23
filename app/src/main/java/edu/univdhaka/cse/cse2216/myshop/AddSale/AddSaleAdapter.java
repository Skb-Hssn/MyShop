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

import edu.univdhaka.cse.cse2216.myshop.R;

public class AddSaleAdapter extends RecyclerView.Adapter<AddSaleViewHolder> {

    private ArrayList<AddSaleItem> items;
    private AddSaleViewHolder viewHolder;
    Context context;

    public AddSaleAdapter(Context context, ArrayList<AddSaleItem> items) {
        this.items = items;
        this.context = context;
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

        AddSaleItem item = items.get(position);

        holder.setItemNameText(item.getItemName());
        holder.setItemQuantityText(item.getItemQuantityText());
        holder.setItemTotalPriceText(item.getItemTotalPriceText());
        holder.setItemNumberText(position+1);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
