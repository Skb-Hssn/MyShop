package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;

public class AddSaleAdapter extends RecyclerView.Adapter<AddSaleViewHolder> {

    private  ArrayList<Item> items;
    private AddSaleViewHolder viewHolder;
    private Cart runningCart;
    Context context;
    TextView totalText;
    TextView payableText;
    public AddSaleAdapter(Context context, Cart cart)
    {
        this.context = context;
        runningCart = cart;
        items = cart.getItemList();
        totalText = (TextView) ((Activity)context).findViewById(R.id.total_amount_add_sale);
        payableText = (TextView) ((Activity)context).findViewById(R.id.payable_amount_add_sale);
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

        Item item = items.get(position);

        holder.setItemNameText(item.getName());
        holder.setItemQuantityText(String.valueOf(item.getSoldQuantity()) + " " + item.getUnit() + " @ " + item.getSoldPrice() + "৳");
        holder.setItemTotalPriceText("৳" + String.valueOf(item.getTotalPrice()));
        holder.setItemNumberText(position+1);

        ConstraintLayout container = holder.itemView.findViewById(R.id.item_details);

        container.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.popup_menu_add_sale);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.add_quantity) {
                            updateAddedProduct(items.get(position), holder);

                        } else if(item.getItemId() == R.id.reduce_quantity) {
                            updateReducedProduct(items.get(position), holder);

                        } else if(item.getItemId() == R.id.delete) {
                            deleteProduct(position);
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    /*
    TODO : Update in firebase
     */

    public void updateAddedProduct(Item item, AddSaleViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quantity ? ");
        EditText editText = new EditText(context);
        editText.setHint("Type added quantity");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editText);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(editText.getText().length() == 0) {
                        editText.requestFocus();
                    }
                    else if(item.getSoldQuantity()+ Double.parseDouble(editText.getText().toString()) > item.getAvailableQuantity())
                    {
                        Toast.makeText(context,"Insufficient amount",Toast.LENGTH_SHORT);
                    }
                    else {
                        double newQuantity = Double.parseDouble(editText.getText().toString());
                        runningCart.setPaidAmount(runningCart.getPaidAmount()+(newQuantity*item.getSoldPrice()));
                        String totalAmount = String.format("%s %s",String.valueOf(runningCart.getTotal()),context.getResources().getString(R.string.taka_logo));
                        totalText.setText(totalAmount);
                        String payAmount = String.format("%s %s",String.valueOf(runningCart.getPaidAmount()),context.getResources().getString(R.string.taka_logo));
                        payableText.setText(payAmount);
                        item.setSoldQuantity(item.getSoldQuantity() + Double.parseDouble(editText.getText().toString()));
                        holder.setItemQuantityText(String.valueOf(item.getSoldQuantity()) + " " + item.getUnit() + " @ " + item.getSoldPrice() + "৳");
                        holder.setItemTotalPriceText("৳" + String.valueOf(item.getTotalPrice()));
                        updateDatabase(item);

                    }
                }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               })
               .show();
    }

    /*
    TODO : Update in firebase
     */

    public void updateReducedProduct(Item item, AddSaleViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quantity ? ");
        EditText editText = new EditText(context);
        editText.setHint("Type reduced quantity");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(editText);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editText.getText().length() == 0) {
                    editText.requestFocus();
                } else if(Double.parseDouble(editText.getText().toString()) > item.getSoldQuantity()) {

                } else {
                    double newQuantity = Double.parseDouble(editText.getText().toString());
                    runningCart.setPaidAmount(runningCart.getPaidAmount()-(newQuantity*item.getSoldPrice()));
                    String totalAmount = String.format("%s %s",String.valueOf(runningCart.getTotal()),context.getResources().getString(R.string.taka_logo));
                    totalText.setText(totalAmount);
                    String payAmount = String.format("%s %s",String.valueOf(runningCart.getPaidAmount()),context.getResources().getString(R.string.taka_logo));
                    payableText.setText(payAmount);
                    item.setSoldQuantity(item.getSoldQuantity() - Double.parseDouble(editText.getText().toString()));
                    holder.setItemQuantityText(String.valueOf(item.getSoldQuantity()) + " " + item.getUnit() + " @ " + item.getSoldPrice() + "৳");
                    holder.setItemTotalPriceText("৳" + String.valueOf(item.getTotalPrice()));
                    updateDatabase(item);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .show();
    }


    /*
       TODO: Update in Firebase
     */
    public void deleteProduct(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        to delete
                        runningCart.setPaidAmount(runningCart.getPaidAmount()-items.get(position).getTotalPrice());
                        String totalAmount = String.format("%s %s",String.valueOf(runningCart.getTotal()),context.getResources().getString(R.string.taka_logo));
                        totalText.setText(totalAmount);
                        String payAmount = String.format("%s %s",String.valueOf(runningCart.getPaidAmount()),context.getResources().getString(R.string.taka_logo));
                        payableText.setText(payAmount);
                        FirebaseDatabase.updateProduct(context,items.get(position));
                        runningCart.removeItem(position);
                        notifyDataSetChanged();
                    }
                })
                .show();
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


    private void updateDatabase(Item item)
    {
        Product itemProduct = null;
        try {
            itemProduct = (Product) ((Product)item).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        itemProduct.setAvailableQuantity(itemProduct.getAvailableQuantity()-item.getSoldQuantity());
        FirebaseDatabase.updateProduct(context,itemProduct);
        Log.d("noman",String.valueOf(itemProduct.getAvailableQuantity()));
    }
}
