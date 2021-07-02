package edu.univdhaka.cse.cse2216.myshop.ProductScreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;

public class ProductActivity extends AppCompatActivity {
    private androidx.appcompat.widget.SearchView productSearchView;
    private RecyclerView productRecyclerView;
    private ProductAdaptor productAdaptor;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3d3d3d")));
        productRecyclerView = (RecyclerView)findViewById(R.id.productList);
        productAdaptor = new ProductAdaptor(this);
        ArrayList<Product> products = new ArrayList<>();
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getProducts(ProductActivity.this,productAdaptor);
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
    }

    private void openAddProductActivity()
    {
        Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
        intent.putExtra("object",productAdaptor.getProducts());
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void workingWithFirebase()
    {
        Cart cart = new Cart();
        cart.setPaidAmount(10);
        cart.setDiscount(5);
        FirebaseDatabase.addCart(ProductActivity.this,cart);

    }


    public static void updateList(ProductAdaptor productAdaptor,ArrayList<Product> products)
    {
        productAdaptor.setList(products);
    }

}