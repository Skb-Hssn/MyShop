package edu.univdhaka.cse.cse2216.myshop.AddSale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.R.id;

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
                                    finish();
                                }
                            }
                    ).setNegativeButton("No", null)
                    .show();
        }
    }

    /*
     * add item
     */
    public void openAddItemDialogue() {
        AddSaleAddItemDialogue dialogue = new AddSaleAddItemDialogue(availableItems);
        dialogue.show(getSupportFragmentManager(), "Add Item");
    }

    /*
     * Discount Dialog opener
     */
    public void openDiscountDialog() {
        DiscountDialog dialog = new DiscountDialog(currentTotal);
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

        adapter = new AddSaleAdapter(this, cart);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    /*
     * discount dialog
     */
    @Override
    public void setDiscount(int amount) {
        discount = amount;
        discountTextView.setText(String.format("%s %s", discount, getResources().getString(R.string.taka_logo)));

        payableAmount = currentTotal - discount;
        payableAmountTextView.setText(String.format("%s %s", payableAmount, getResources().getString(R.string.taka_logo)));
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

    }

    /*
     * Done Button
     */
    public void setDone() {
        updateDataBase();
        done = true;
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}