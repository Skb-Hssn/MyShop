package edu.univdhaka.cse.cse2216.myshop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    private CartAdaptor cartAdaptor;
    private RecyclerView cartRecycelerView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cartAdaptor = new CartAdaptor(HistoryActivity.this);
        ArrayList<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart());
        carts.add(new Cart());
        Cart cart = new Cart(100);
        cart.addItem(new Product("a","b","aa",10,100));
        cart.addItem(new Product("c","b","aa",10,100));
        cart.addItem(new Product("c","b","aa",10,100));
        carts.add(cart);
        cartAdaptor.set(carts);
        cartRecycelerView = (RecyclerView)findViewById(R.id.cardRecuycelerView);
        cartRecycelerView.setAdapter(cartAdaptor);
        cartRecycelerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.history_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.filter)
        {
            SelectDate selectDate = new SelectDate(cartAdaptor);
            selectDate.show(getSupportFragmentManager(),"Choose Date");
        }
        return super.onOptionsItemSelected(item);
    }
}