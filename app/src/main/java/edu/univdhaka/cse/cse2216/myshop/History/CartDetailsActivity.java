package edu.univdhaka.cse.cse2216.myshop.History;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.R;

public class CartDetailsActivity extends AppCompatActivity {
    private TextView totalText;
    private TextView discountText;
    private TextView paidText;
    private RecyclerView cartHistoryRecyclerView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Cart cart = (Cart) bundle.get("cartObject");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        totalText = (TextView)findViewById(R.id.totalInCartText);
        discountText = (TextView)findViewById(R.id.discountInCartText);
        paidText = (TextView)findViewById(R.id.paidInCartText);

        String total = String.format("%.2f %s", (cart.getDiscount()+cart.getPaidAmount()),getResources().getString(R.string.taka_logo));
        totalText.setText(total);

        String discount = String.format("%.2f %s", (cart.getDiscount()),getResources().getString(R.string.taka_logo));
        discountText.setText(discount);

        String paidAmount = String.format("%.2f %s", (cart.getPaidAmount()),getResources().getString(R.string.taka_logo));
        paidText.setText(paidAmount);
        cartHistoryRecyclerView = (RecyclerView)findViewById(R.id.cartHistoryRecycelerView);

        ArrayList<Item> items = cart.getItemList();

        ProductAdaptorInCart productAdaptorInCart = new ProductAdaptorInCart(this,items);
        cartHistoryRecyclerView.setAdapter(productAdaptorInCart);
        cartHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}