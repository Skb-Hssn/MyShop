package edu.univdhaka.cse.cse2216.myshop.History;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.R;

public class ProductAdaptorInCart extends RecyclerView.Adapter<ProductAdaptorInCart.CartViewHolder> {
    private Context context;
    private ArrayList<Item> items;
    TextView nameText,quantityText,totalPriceText;
    public ProductAdaptorInCart(Context context, ArrayList<Item> items)
    {
        this.context = context;
        this.items = items;

    }


    @NonNull
    @NotNull
    @Override
    public ProductAdaptorInCart.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_in_cart,parent,false);
        return new CartViewHolder(view);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductAdaptorInCart.CartViewHolder holder, int position) {
            String itemNameWithCompany = String.format("%s %s%s",items.get(position).getName()," © ",items.get(position).getCompanyName());
            nameText.setText(itemNameWithCompany);

            String quantityWithUnit = String.format("%.2f %s", (items.get(position).getSoldQuantity()),items.get(position).getUnit());
            quantityText.setText(quantityWithUnit);

            totalPriceText.setText(String.format("%.2f %s", (items.get(position).getTotalPrice()),context.getString(R.string.taka_logo)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class
    CartViewHolder extends RecyclerView.ViewHolder {
        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameText = (TextView)itemView.findViewById(R.id.nameInCartDeatail);
            quantityText = (TextView)itemView.findViewById(R.id.quantityInCartDetail);
            totalPriceText = (TextView)itemView.findViewById(R.id.priceInCartDeatail);
        }
    }
}
