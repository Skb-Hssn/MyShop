package edu.univdhaka.cse.cse2216.myshop.AddSale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.ItemAdaptor;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.ProductScreen.ProductActivity;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.R.id;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSale extends AppCompatActivity implements AddSaleAddItemDialogue.AddSaleAddItemDialogueListener, DiscountDialog.DiscountDialogListener{

    private RecyclerView recyclerView;
    private AddSaleAdapter adapter;
    private FloatingActionButton addItemButtom;
    private ArrayList<Pair<String, Integer>> availableItems = new ArrayList<>();
    private TextView totalAmountTextView;
    private TextView discountTextView;
    private TextView payableAmountTextView;
    private AppCompatButton discountButton;
    private AppCompatButton doneButton;
    private AppCompatButton backButton;
//    noman

    Cart newCart = new Cart();
//    ArrayList<Item> itemList = newCart.getItemList();
    private int currentTotal = 0;
    private int discount = 0;
    private int payableAmount = 0;

    private boolean done = false;

    ArrayList<AddSaleItem> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        getAvailableItems();

        createRecyclerView();

        addItemButtom = findViewById(R.id.add_item_button);
        discountButton = findViewById(R.id.discount_button);
        doneButton = findViewById(R.id.done_button);
        backButton = findViewById(R.id.back_button_add_sale);
        totalAmountTextView = findViewById(R.id.total_amount_add_sale);
        discountTextView = findViewById(R.id.discount_add_sale);
        payableAmountTextView = findViewById(R.id.payable_amount_add_sale);


        addItemButtom.setOnClickListener(v -> openAddItemDialogue());
        discountButton.setOnClickListener(v -> openDiscountDialog());
        backButton.setOnClickListener(v -> onBackPressed());
        doneButton.setOnClickListener(v -> setDone());
    }

    @Override
    public void onBackPressed() {
        if(done) {
            super.onBackPressed();
            finish();
        } else {
            AlertDialog builder = new AlertDialog.Builder(this)
                    .setMessage("Cart has not been saved. Continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AddSale.super.onBackPressed();
                                    updateDatabaseWhenCancel();
                                    finish();
                                }
                            }
                    ).setNegativeButton("No", null)
                    .show();
        }
    }

    private void updateDatabaseWhenCancel() {
        for(Item item : adapter.getItems())
        {
            Product itemProduct = (Product)item;

            FirebaseDatabase.updateJust(itemProduct);
//            Log.d("noman","product");
            Log.d("noman",String.valueOf(itemProduct.getAvailableQuantity()));

        }
    }

    /*
     * add item
     */
    public void openAddItemDialogue() {
            seeItem();
//        AddSaleAddItemDialogue dialogue = new AddSaleAddItemDialogue(availableItems);
//        dialogue.show(getSupportFragmentManager(), "Add Item");
    }
    public void seeItem()
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AddSale.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sale_items,null);
        builder.setView(view);
        RecyclerView itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchItem);
        ItemAdaptor itemAdaptor = new ItemAdaptor(AddSale.this,adapter);


        FirebaseDatabase.getProducts(builder.getContext(),itemAdaptor);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdaptor.getFilter().filter(newText);
                return false;
            }
        });

        itemRecyclerView.setAdapter(itemAdaptor);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(AddSale.this));
        AlertDialog dialog = builder.create();

        dialog.show();
        ImageButton closeButton = (ImageButton)view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                totalAmountTextView.setText(String.valueOf(newCart.getTotal()));
                payableAmountTextView.setText(String.valueOf(newCart.getPaidAmount()));
            }
        });

    }

    /*
     * Discount Dialog opener
     */
    public void openDiscountDialog() {
        DiscountDialog dialog = new DiscountDialog(newCart.getTotal());
        dialog.show(getSupportFragmentManager(), "Discount");
    }

    /*
     * Get the item that has been added to cart
     */
    @Override
    public void applyTexts(String itemName, String itemQuantity) {
        insertItem(itemName, Integer.parseInt(itemQuantity));
    }


    /*
     * TODO: Get list of available items with quantity from FIREBASE
     */
    private void getAvailableItems() {
        for (int i = 0; i < 5; i++) {
            String s = new String("aaa" + Integer.toString(i*i));
            availableItems.add(new Pair<String, Integer>(s, 3*i));
        }
    }

    /*
     * Insert item to Cart.
     */
    public void insertItem(String itemName, int itemQuantity) {
        done = false;

        AddSaleItem item = new AddSaleItem(itemName, itemQuantity, 500);
        cart.add(item);
        adapter.notifyItemInserted(cart.size()-1);
        currentTotal += item.getItemTotalPrice();

        totalAmountTextView.setText(
                String.format("%s %s", currentTotal, getResources().getString(R.string.taka_logo))
        );

        payableAmountTextView.setText(
                String.format("%s %s", currentTotal, getResources().getString(R.string.taka_logo))
        );
    }

    /*
     *  Recycler view for items of the Cart.
     */
    public void createRecyclerView() {
        recyclerView = findViewById(R.id.add_sale_recycler_view);
        recyclerView.setHasFixedSize(true);

//        adapter = new AddSaleAdapter(this, cart);
        adapter = new AddSaleAdapter(AddSale.this,newCart);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    /*
     * discount dialog
     */
    @Override
    public void setDiscount(int amount) {

        discount += amount;
        discountTextView.setText(String.format("%s %s", discount, getResources().getString(R.string.taka_logo)));


        newCart.setDiscount(amount);
        payableAmountTextView.setText(String.valueOf(newCart.getPaidAmount()));
    }

    /*
     * Update the current remaining items
     */
    public void updateItems() {

    }


    /*
     * Set the updated itemList Firebase after done.
     */
    public void updateDataBase() {
        FirebaseDatabase.addCart(AddSale.this,newCart);
    }

    /*
     * Done Button
     */
    public void setDone() {
        Log.d("amount",String.valueOf(newCart.getTotal()));
        if(String.valueOf(newCart.getTotal()).compareTo("0.0") == 0)
        {
            finish();
            return;
        }
        updateDataBase();
        done = true;
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}