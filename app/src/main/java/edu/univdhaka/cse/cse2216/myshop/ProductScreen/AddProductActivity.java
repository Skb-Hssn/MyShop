package edu.univdhaka.cse.cse2216.myshop.ProductScreen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.ShopKeeper;

public class AddProductActivity extends AppCompatActivity {
    private EditText productNameText,companyNameText,unitText,quantityText,priceText;
    private Button productSaveButton;
    ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productNameText = (EditText)findViewById(R.id.productNameEditBox);
        companyNameText = (EditText)findViewById(R.id.companyNameEditBox);
        unitText = (EditText)findViewById(R.id.unitEditBox);
        quantityText = (EditText)findViewById(R.id.quantityEditBox);
        priceText = (EditText)findViewById(R.id.priceEditBox);
        productSaveButton = (Button)findViewById(R.id.productSaveButton);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        products = (ArrayList<Product>)bundle.get("object");
        productSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct()
    {
        String name,companyName,unit;
        double quantity,price;
        name = productNameText.getText().toString();
        companyName = companyNameText.getText().toString();
        unit = unitText.getText().toString();

        if(name.isEmpty())
        {
            productNameText.requestFocus();
            Toast.makeText(AddProductActivity.this,"Give Product Name",Toast.LENGTH_SHORT).show();
        }
        else if(companyName.isEmpty())
        {
            companyNameText.requestFocus();
            Toast.makeText(AddProductActivity.this,"Give Company Name",Toast.LENGTH_SHORT).show();
        }
        else if(unit.isEmpty())
        {
            unitText.requestFocus();
            Toast.makeText(AddProductActivity.this,"Give Unit",Toast.LENGTH_SHORT).show();
        }
        else if(quantityText.getText().toString().isEmpty())
        {
            quantityText.requestFocus();
            Toast.makeText(AddProductActivity.this,"Give Quantity",Toast.LENGTH_SHORT).show();
        }
        else if(priceText.getText().toString().isEmpty())
        {
            priceText.requestFocus();
            Toast.makeText(AddProductActivity.this,"Give Sold Price",Toast.LENGTH_SHORT).show();
        }
        else
        {
            quantity = Double.parseDouble(quantityText.getText().toString());
            price = Double.parseDouble(priceText.getText().toString());
            Product product = new Product(name,companyName,unit,quantity,price);
            for(Product product1 : products)
            {
                if(product1.getName().toLowerCase().compareTo(product.getName().toLowerCase()) == 0 && product1.getCompanyName().toLowerCase().compareTo(product.getCompanyName().toLowerCase()) == 0 && (int) product1.getSoldPrice() == (int)product.getSoldPrice() )
                {
                    Toast.makeText(AddProductActivity.this,"Already exist",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            products.add(product);
            Toast.makeText(AddProductActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            FirebaseDatabase.addProduct(AddProductActivity.this,product);
            this.finish();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void workingWithFirebase()
    {
        String email = "nsakhawathhossan29@gmail.com";
        ShopKeeper shopKeeper = new ShopKeeper("Noman","MyShop",email);
        String password = "noman123";

        ArrayList<Item> products = new ArrayList<>();
        Product product = new Product("noman","komolo","a",4,5);
        products.add(new Item(product,10,15));
        products.add(new Item(new Product("noman","komolo","a",4,5),15,30));

        Cart cart = new Cart();
        cart.setItemList(products);
        cart.setPaidAmount(100);
        cart.setDiscount(20);
        FirebaseDatabase.addCart(AddProductActivity.this,cart);
    }
}