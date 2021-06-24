package edu.univdhaka.cse.cse2216.myshop.ProductScreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Objects;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.ItemAdaptor;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.ShopKeeper;

public class ProductActivity extends AppCompatActivity {
    private androidx.appcompat.widget.SearchView productSearchView;
    private RecyclerView productRecyclerView;
    private ProductAdaptor productAdaptor;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        productRecyclerView = (RecyclerView)findViewById(R.id.productList);
        productAdaptor = new ProductAdaptor(this);
                ArrayList<Product> products = new ArrayList<>();
                products.add(new Product("sakib","aa","aaa",10,10));
                products.add(new Product("ab","aa","aaa",10,100));
                products.add(new Product("noman","aa","aaa",10,100));
                productAdaptor.setList(products);
        productRecyclerView.setAdapter(productAdaptor);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//                workingWithFirebase();
//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
        Log.d("noman","restart");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("noman","start");
        FirebaseDatabase.getProducts(ProductActivity.this,productAdaptor);

    }

    @Override
    protected void onPostResume() {
        Log.d("noman","resume");
        super.onPostResume();
    }

    private void openAddProductActivity()
    {
        Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
        intent.putExtra("object",productAdaptor.getProducts());
        startActivity(intent);
        //        seeItem();
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