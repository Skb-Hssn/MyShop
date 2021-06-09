package edu.univdhaka.cse.cse2216.myshop;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.widget.SearchView.*;

public class ProductActivity extends AppCompatActivity {
    private androidx.appcompat.widget.SearchView productSearchView;
    private RecyclerView productRecyclerView;
    private ProductAdaptor productAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        productRecyclerView = (RecyclerView)findViewById(R.id.productList);
        productAdaptor = new ProductAdaptor(this);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("sakib","aa","aaa",10,10));
        products.add(new Product("ab","aa","aaa",10,100));
        products.add(new Product("noman","aa","aaa",10,100));
        productAdaptor.setList(products);
        productRecyclerView.setAdapter(productAdaptor);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.product_activity_menu,menu);
        productSearchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.searchProduct).getActionView();
        productSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productAdaptor.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addProductActivityButton:
                openAddProductActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void openAddProductActivity()
    {
        Intent intent = new Intent(ProductActivity.this,AddProductActivity.class);
        startActivity(intent);
    }
}