package edu.univdhaka.cse.cse2216.myshop;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder> {
    private ArrayList<Product> productInList;
    private ArrayList<Product> products;
    private TextView nameTextView;
    private TextView companyNameTextView;
    private TextView stockTextView;
    private TextView priceTextView;
    private ConstraintLayout container;
    private Context context;
    public ProductAdaptor(Context context)
    {
        this.context = context;
        productInList = new ArrayList<>();
    }
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.signle_product_display,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ProductAdaptor.ProductViewHolder holder, int position) {
            Product product = productInList.get(position);
            nameTextView.setText(product.getName());
            companyNameTextView.setText(product.getCompanyName());
            stockTextView.setText(String.valueOf(product.getAvailableQuantity())+" "+product.getUnit());
            priceTextView.setText(String.valueOf(product.getSoldPrice()) + " "+"Tk");
            container.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    PopupMenu editProductPopUp = new PopupMenu(context,v);
                    editProductPopUp.inflate(R.menu.popup_menu_edit_product);
                    editProductPopUp.setGravity(Gravity.CENTER);
                    editProductPopUp.show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return productInList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ProductViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            nameTextView = (TextView)itemView.findViewById(R.id.productNameTextView);
            companyNameTextView = (TextView)itemView.findViewById(R.id.companyNameTextView);
            stockTextView = (TextView)itemView.findViewById(R.id.stockTextView);
            priceTextView = (TextView)itemView.findViewById(R.id.priceTextView);
            container = (ConstraintLayout)itemView.findViewById(R.id.productContainer);

        }
    }
    public void setList(ArrayList<Product> products)
    {
        this.productInList = products;
        this.products = products;
        notifyDataSetChanged();
    }
}
