package edu.univdhaka.cse.cse2216.myshop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private TextView daySaleText,dateText;
    private ImageFilterButton addCart;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home_icon);
        daySaleText = (TextView)findViewById(R.id.daySaleTextView);
        dateText = (TextView)findViewById(R.id.homeDateTextView);
        addCart = (ImageFilterButton) findViewById(R.id.addCartButton);
        String date = LocalDate.now().toString();
        dateText.setText(date);
        FirebaseDatabase.signIn("nsakhawathhossan29@gmail.com","noman123",HomeActivity.this);
        FirebaseDatabase.getDaySaleTotal(HomeActivity.this,daySaleText,date);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCartActivity();
            }
        });

    }

    private void openCartActivity() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.seeProductButton:
                openProductActivity();
                break;
            case R.id.seeHistoryButton:
                openHistoryActivity();
                break;
            case R.id.seeAbout:
                openAboutActivity();
                break;
            case R.id.signOut:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseDatabase.signOut();
    }

    private void openAboutActivity() {
    }

    private void openHistoryActivity() {
        Intent intent = new Intent(HomeActivity.this,HistoryActivity.class);
        startActivity(intent);
    }

    private void openProductActivity() {
        Intent intent = new Intent(HomeActivity.this,ProductActivity.class);
        startActivity(intent);
    }
}