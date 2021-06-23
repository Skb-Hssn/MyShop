package edu.univdhaka.cse.cse2216.myshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
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

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Product> availableItems;
    private ArrayList<Item> soldItems;
    private double cost = 0;
    public ItemAdaptor(Context context)
    {
        this.context = context;
        availableItems = new ArrayList<>();
        soldItems = new ArrayList<>();
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemAdaptor.ViewHolder holder, int position) {
        TextView nameText,companyNameText,quantityText,priceText;
        ImageButton addButton = (ImageButton)holder.itemView.findViewById(R.id.itemAddButton);
        nameText = (TextView)holder.itemView.findViewById(R.id.itemName);
        companyNameText = (TextView)holder.itemView.findViewById(R.id.itemCompany);
        quantityText = (TextView)holder.itemView.findViewById(R.id.itemQuantity);
        priceText = (TextView)holder.itemView.findViewById(R.id.itemPrice);
        nameText.setText(availableItems.get(position).getName());
        companyNameText.setText(availableItems.get(position).getCompanyName());
        quantityText.setText(String.valueOf(availableItems.get(position).getAvailableQuantity())+" "+availableItems.get(position).getUnit());
        priceText.setText(String.valueOf(availableItems.get(position).getSoldPrice())+" $");
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
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityString = editText.getText().toString();
                if(quantityString.isEmpty())
                {
                    Toast.makeText(context,"Add quantity",Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(quantityString) > product.getAvailableQuantity())
                {
                    Toast.makeText(context,"Insufficient product",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double quantity,price,itemPrice;
                    itemPrice = product.getSoldPrice();
                    quantity = Double.parseDouble(quantityString);
                    price = itemPrice*quantity;
//                    cost = price;
//                    Product cartProduct = new Product(product.getName(), product.getCompanyName(), product.getUnit(),quantity,price);
                    Item cartItem = new Item(product,quantity,price);
                    soldItems.add(cartItem);
                    product.setAvailableQuantity(product.getAvailableQuantity()-quantity);
                    notifyDataSetChanged();
//                    FirebaseDatabase.updateJust(product);
                    FirebaseDatabase.updateProduct(context,product);

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
                if(text.isEmpty())
                {
                    FirebaseDatabase.getProducts(context,ItemAdaptor.this);
                }
                else {
                    for(Product product : availableItems)
                    {
                        if(product.getName().toLowerCase().contains(text))
                        {
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
        }
    }
    public void setList(ArrayList<Product> products)
    {
        this.availableItems = products;
        notifyDataSetChanged();

    }

    public ArrayList<Item> getSoldItems() {
        return soldItems;
    }
}
