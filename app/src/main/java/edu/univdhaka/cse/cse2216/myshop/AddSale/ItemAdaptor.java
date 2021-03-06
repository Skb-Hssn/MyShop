package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Product> availableItems;
    private AddSaleAdapter addSaleAdapter;
    private ArrayList<Product> soldProducts;
    private ArrayList<Product> originalList;

    TextView nameText;
    TextView companyNameText;
    TextView quantityText;
    TextView priceText;

    public ItemAdaptor(Context context, AddSaleAdapter addSaleAdapter)
    {
        this.context = context;
        availableItems = new ArrayList<>();
        this.addSaleAdapter = addSaleAdapter;
        soldProducts = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemAdaptor.ViewHolder holder, int position) {
        ImageButton addButton = (ImageButton)holder.itemView.findViewById(R.id.itemAddButton);
        nameText = (TextView)holder.itemView.findViewById(R.id.itemName);
        companyNameText = (TextView)holder.itemView.findViewById(R.id.itemCompany);
        quantityText = (TextView)holder.itemView.findViewById(R.id.itemQuantity);
        priceText = (TextView)holder.itemView.findViewById(R.id.itemPrice);

        nameText.setText(availableItems.get(position).getName());
        companyNameText.setText(availableItems.get(position).getCompanyName());
        quantityText.setText(String.format("%.2f %s", availableItems.get(position).getAvailableQuantity(), availableItems.get(position).getUnit()));
        priceText.setText(String.format("%.2f Tk", availableItems.get(position).getSoldPrice()));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(availableItems.get(position));
            }
        });
    }

    public void addProduct(Product product)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quantity ? ");
        EditText editText = new EditText(context);
        editText.setHint("Type quantity");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editText);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityString = editText.getText().toString();
                if(quantityString.isEmpty()) {
                    Toast.makeText(context,"Add quantity",Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(quantityString) > product.getAvailableQuantity()) {
                    Toast.makeText(context,"Insufficient product",Toast.LENGTH_SHORT).show();
                }
                else {
                    double quantity,price,itemPrice;
                    itemPrice = product.getSoldPrice();
                    quantity = Double.parseDouble(quantityString);
                    price = itemPrice*quantity;

                    Item cartItem = new Item(product,quantity,price);
                    boolean flag = false;

                    for (Product product1 : addSaleAdapter.getItems()) {
                        if(product1 == product) {
                            flag = true;
                            break;
                        }
                    }

                    if(!flag) {
                        cartItem.setAvailableQuantity(product.getAvailableQuantity() - quantity);

                        soldProducts.add(product);
                        addSaleAdapter.addItem(cartItem);
                        availableItems.remove(product);
                        product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
                        notifyDataSetChanged();

                        FirebaseDatabase.updateProduct(context, product);
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return availableItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String text = constraint.toString();
                ArrayList<Product> temp = new ArrayList<>();
                if(text.isEmpty()) {
                    temp.addAll(originalList);
                } else {
                    for(Product product : originalList) {
                        if(product.getName().toLowerCase().contains(text)) {
                            temp.add(product);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = temp;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                availableItems = (ArrayList<Product>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ImageButton addButton = (ImageButton)itemView.findViewById(R.id.itemAddButton);
            nameText = (TextView)itemView.findViewById(R.id.itemName);
            companyNameText = (TextView)itemView.findViewById(R.id.itemCompany);
            quantityText = (TextView)itemView.findViewById(R.id.itemQuantity);
        }
    }

    public void setList(ArrayList<Product> products)
    {
        for(Product x : addSaleAdapter.getItems()) {
            for(Product y : products) {
                if(x.compareTo(y) == 0) {
                    products.remove(y);
                    break;
                }
            }
        }

        this.availableItems = products;
        Collections.sort(availableItems);
        this.originalList = new ArrayList<>();
        originalList.addAll(products);
        this.notifyDataSetChanged();
    }
}
