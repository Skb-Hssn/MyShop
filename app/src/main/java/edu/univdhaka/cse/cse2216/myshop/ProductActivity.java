package edu.univdhaka.cse.cse2216.myshop;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
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
//        ArrayList<Product> products = new ArrayList<>();
//        products.add(new Product("sakib","aa","aaa",10,10));
//        products.add(new Product("ab","aa","aaa",10,100));
//        products.add(new Product("noman","aa","aaa",10,100));
//        productAdaptor.setList(products);
        productRecyclerView.setAdapter(productAdaptor);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        workingWithFirebase();



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
        Intent intent = new Intent(ProductActivity.this,AddProductActivity.class);
        intent.putExtra("object",productAdaptor.getProducts());
        startActivity(intent);
//        seeItem();
    }
    private void workingWithFirebase()
    {
        String email = "nsakhawathhossan29@gmail.com";
        ShopKeeper shopKeeper = new ShopKeeper("Noman","MyShop",email);
        String password = "noman123";
//        FirebaseDatabase.signOut();
//        FirebaseDatabase.signUp(shopKeeper,password,ProductActivity.this);
//        FirebaseDatabase.signIn(email,password,ProductActivity.this);
//        Log.d("noman",String.valueOf(FirebaseDatabase.isValidUser()));
//        FirebaseDatabase.pro(ProductActivity.this);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("noman","komolo","a",4,5));
        products.add(new Product("noman","komolo","a",4,5));
        products.add(new Product("noman","komolo","a",4,5));
        products.add(new Product("noman","komolo","a",4,5));
        products.add(new Product("noman","komolo","a",4,5));

    }
    public static void updateList(ProductAdaptor productAdaptor,ArrayList<Product> products)
    {
        productAdaptor.setList(products);
    }
    public void seeItem()
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sale_items,null);
        builder.setView(view);
        RecyclerView itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchItem);
        ItemAdaptor itemAdaptor =new ItemAdaptor(ProductActivity.this);


        FirebaseDatabase.getProducts(builder.getContext(),itemAdaptor);

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
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
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        AlertDialog dialog = builder.create();

        dialog.show();
        ImageButton closeButton = (ImageButton)view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}