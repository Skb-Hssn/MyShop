package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import edu.univdhaka.cse.cse2216.myshop.R;

public class AddSaleViewHolder extends RecyclerView.ViewHolder {

    private TextView itemName;
    private TextView itemQuantity;
    private TextView itemTotalPrice;
    private TextView itemNumber;

    public AddSaleViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        itemQuantity = itemView.findViewById(R.id.item_quantity);
        itemTotalPrice = itemView.findViewById(R.id.item_total_price);
        itemNumber = itemView.findViewById(R.id.item_number);
    }

    public void setItemNameText(String itemName) {
        this.itemName.setText(itemName);
    }

    public void setItemQuantityText(String itemQuantity) {
        this.itemQuantity.setText(itemQuantity);
    }

    public void setItemTotalPriceText(String itemTotalPrice) {
        this.itemTotalPrice.setText(itemTotalPrice);
    }

    @SuppressLint("DefaultLocale")
    public void setItemNumberText(int itemNumber) {
        this.itemNumber.setText(String.format("%d.", itemNumber));
    }
}
