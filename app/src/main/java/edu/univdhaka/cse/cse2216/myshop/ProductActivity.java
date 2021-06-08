package edu.univdhaka.cse.cse2216.myshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private SearchView productSearchView;
    private RecyclerView productRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        productRecyclerView = (RecyclerView)findViewById(R.id.productList);
        ProductAdaptor productAdaptor = new ProductAdaptor(this);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("a","aa","aaa",10,10));
        products.add(new Product("ab","aa","aaa",10,100));
        productAdaptor.setList(products);
        productRecyclerView.setAdapter(productAdaptor);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.product_activity_menu,menu);
        productSearchView = (SearchView) menu.findItem(R.id.searchProduct).getActionView();
        return super.onCreateOptionsMenu(menu);
    }
}