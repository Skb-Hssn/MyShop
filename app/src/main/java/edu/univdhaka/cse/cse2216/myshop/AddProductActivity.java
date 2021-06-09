package edu.univdhaka.cse.cse2216.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {
    private EditText productNameText,companyNameText,unitText,quantityText,priceText;
    private Button productSaveButton;
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
//            quantity = Double.parseDouble(quantityText.getText().toString());
//            price = Double.parseDouble(priceText.getText().toString());
//            Log.d("noman",quantityText.getText().toString());
            Toast.makeText(AddProductActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}