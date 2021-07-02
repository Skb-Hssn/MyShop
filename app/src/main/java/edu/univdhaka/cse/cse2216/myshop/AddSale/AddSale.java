package edu.univdhaka.cse.cse2216.myshop.AddSale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSale extends AppCompatActivity implements DiscountDialog.DiscountDialogListener{

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

    Cart newCart = new Cart();
    private double discount = 0;

    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        addItemButtom = findViewById(R.id.add_item_button);
        discountButton = findViewById(R.id.discount_button);
        doneButton = findViewById(R.id.done_button);
        backButton = findViewById(R.id.back_button_add_sale);
        totalAmountTextView = findViewById(R.id.total_amount_add_sale);
        discountTextView = findViewById(R.id.discount_add_sale);
        payableAmountTextView = findViewById(R.id.payable_amount_add_sale);

        createRecyclerView();

        addItemButtom.setOnClickListener(v -> seeItem());
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

            itemProduct.setAvailableQuantity(item.getAvailableQuantity() + item.getSoldQuantity());

            FirebaseDatabase.updateJust(itemProduct);
        }
    }


    public void seeItem()
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(AddSale.this);
        LayoutInflater layoutInflater = getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.sale_items,null);
        builder.setView(view);

        RecyclerView itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchItem);
        ItemAdaptor itemAdaptor = new ItemAdaptor(AddSale.this, adapter);

        FirebaseDatabase.getProducts(builder.getContext(), itemAdaptor);

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

            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String totalAmount = String.format("%.2f %s", newCart.getTotal(),getResources().getString(R.string.taka_logo));
                totalAmountTextView.setText(totalAmount);

                String payAmount = String.format("%.2f %s", newCart.getPaidAmount(),getResources().getString(R.string.taka_logo));
                payableAmountTextView.setText(payAmount);
            }
        });
    }


    public void openDiscountDialog() {
        DiscountDialog dialog = new DiscountDialog(newCart.getTotal());
        dialog.show(getSupportFragmentManager(), "Discount");
    }


    public void createRecyclerView() {
        recyclerView = findViewById(R.id.add_sale_recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new AddSaleAdapter(AddSale.this, newCart, totalAmountTextView, payableAmountTextView, discountTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void setDiscount(double amount) {

        discount = amount;
        discountTextView.setText(String.format("%.2f %s", discount, getResources().getString(R.string.taka_logo)));

        double currentTotal = 0;

        if(totalAmountTextView != null) {
            currentTotal = Double.parseDouble(totalAmountTextView.getText().toString().split(" ")[0]);
        }

        newCart.setDiscount(amount);
        newCart.setPaidAmount(currentTotal - discount);

        payableAmountTextView.setText(String.format("%.2f %s", newCart.getPaidAmount(),getResources().getString(R.string.taka_logo)));
    }

    public void updateDataBase() {
        FirebaseDatabase.addCart(AddSale.this,newCart);
    }


    public void setDone() {
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}