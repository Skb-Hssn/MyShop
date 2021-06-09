package edu.univdhaka.cse.cse2216.myshop;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder> implements Filterable {
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
        Log.d("noman",product.getName());

        TextView nameTextView = (TextView)holder.itemView.findViewById(R.id.productNameTextView);
        TextView companyNameTextView = (TextView)holder.itemView.findViewById(R.id.companyNameTextView);
        TextView stockTextView = (TextView)holder.itemView.findViewById(R.id.stockTextView);
        TextView priceTextView = (TextView)holder.itemView.findViewById(R.id.priceTextView);
        ConstraintLayout container = (ConstraintLayout)holder.itemView.findViewById(R.id.productContainer);
            nameTextView.setText(productInList.get(position).getName());
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
                    editProductPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.addQuantity:
                                    Log.d("noman","add");
                                    break;
                                case R.id.reduceQuantity:
                                    Log.d("noman","reduce");
                                    break;
                                case R.id.changePrice:
                                    Log.d("noman","change");
                                    break;
                            }
                            return false;
                        }
                    });
                    editProductPopUp.show();

                }
            });
    }
    private void updateProduct(Product product)
    {

    }
    private void addProduct(Product product)
    {

    }
    @Override
    public int getItemCount() {
        return productInList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults;
                String queryText = constraint.toString();
                ArrayList<Product> newList = new ArrayList<>();
                if(queryText.isEmpty())
                {
                    newList = products;
                }
                else
                {
                    for(Product product : products)
                    {
                        if(product.getName().toLowerCase().contains(queryText.toLowerCase()))
                        {
                            newList.add(product);
                        }
                    }
                }
                filterResults = new FilterResults();
                filterResults.values = newList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                productInList.clear();
//                productInList.addAll((Collection<? extends Product>) results.values);
                productInList = (ArrayList<Product>)results.values;
                Log.d("noman",String.valueOf(productInList.size()));
                for(Product product : productInList)
                {
                    Log.d("noman",product.getName());
                }
                notifyDataSetChanged();
            }
        };
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
