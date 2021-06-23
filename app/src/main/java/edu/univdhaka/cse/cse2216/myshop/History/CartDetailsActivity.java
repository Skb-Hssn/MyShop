package edu.univdhaka.cse.cse2216.myshop.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.ProductAdaptorInCart;
import edu.univdhaka.cse.cse2216.myshop.ProductScreen.ProductAdaptor;
import edu.univdhaka.cse.cse2216.myshop.R;

public class CartDetailsActivity extends AppCompatActivity {
    private TextView totalText,discountText,paidText;
    private RecyclerView cartHistoryRecyclerView;
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
        totalText.setText(String.valueOf(cart.getDiscount()+cart.getPaidAmount())+" Tk");
        discountText.setText(String.valueOf(cart.getDiscount())+" Tk");
        paidText.setText(String.valueOf(cart.getPaidAmount()) +" ");
        cartHistoryRecyclerView = (RecyclerView)findViewById(R.id.cartHistoryRecycelerView);
        ArrayList<Item> items = cart.getItemList();
        Log.d("salman", String.valueOf(items.size()));

        ProductAdaptorInCart productAdaptorInCart = new ProductAdaptorInCart(this,items);
        cartHistoryRecyclerView.setAdapter(productAdaptorInCart);
        cartHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}