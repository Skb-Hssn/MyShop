package edu.univdhaka.cse.cse2216.myshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
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
//                                    updateProduct();
                                    updateAddedProduct(productInList.get(position),position);
                                    break;
                                case R.id.reduceQuantity:
//                                    updateProduct();
                                    Log.d("noman","reduce");
                                    updateReducedProduct(productInList.get(position),position);
                                    break;
                                case R.id.changePrice:
                                    Log.d("noman","change");
                                    updatePrice(productInList.get(position),position);
                                    break;
                            }
                            return false;
                        }
                    });
                    editProductPopUp.show();

                }
            });
    }
    private void updateAddedProduct(Product product,int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quantity ? ");
        EditText editText = new EditText(context);
        editText.setHint("Type newly added quantity");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityText;
                quantityText = editText.getText().toString();
                if(quantityText.isEmpty())
                {
                    Toast.makeText(context,"Give Quantity",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double quantity = Double.parseDouble(quantityText);
                    product.setAvailableQuantity(product.getAvailableQuantity()+quantity);
                    FirebaseDatabase.updateProduct(context,product,ProductAdaptor.this,position);

                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void updateReducedProduct(Product product,int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quantity ? ");
        EditText editText = new EditText(context);
        editText.setHint("Type reduced quantity");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityText;
                quantityText = editText.getText().toString();
                if(quantityText.isEmpty())
                {
                    Toast.makeText(context,"Give Quantity",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double quantity = Double.parseDouble(quantityText);
                    if(quantity > product.getAvailableQuantity())
                    {
                        Toast.makeText(context,"Not sufficient Quantity",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        product.setAvailableQuantity(product.getAvailableQuantity()-quantity);
                        FirebaseDatabase.updateProduct(context,product,ProductAdaptor.this,position);
                    }


                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void updatePrice(Product product,int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Price");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        EditText priceBox = new EditText(context);
        priceBox.setHint("new Price");
        EditText quantityBox = new EditText(context);
        quantityBox.setHint("Quantity with this price");
        quantityBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        priceBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(priceBox);
        linearLayout.addView(quantityBox);
        builder.setView(linearLayout);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityText,priceText;
                quantityText = quantityBox.getText().toString();
                priceText = priceBox.getText().toString();
                if(priceText.isEmpty())
                {
                    Toast.makeText(context,"Give price",Toast.LENGTH_SHORT).show();

                }
                else if(quantityText.isEmpty())
                {
                    Toast.makeText(context,"GiveQuantity",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    product.setAvailableQuantity(Double.parseDouble(quantityText));
                    product.setSoldPrice(Double.parseDouble(priceText));
                    FirebaseDatabase.addExistingProduct(context,product,ProductAdaptor.this);
                    Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
                }


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    public void update(int position,Product product)
    {
        productInList.add(position,product);
        notifyDataSetChanged();
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

    public class ProductViewHolderInCart {
    }
}
