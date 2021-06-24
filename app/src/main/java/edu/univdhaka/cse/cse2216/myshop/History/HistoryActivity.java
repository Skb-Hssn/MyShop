package edu.univdhaka.cse.cse2216.myshop.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.SelectDate;

public class HistoryActivity extends AppCompatActivity {
    private CartAdaptor cartAdaptor;
    private RecyclerView cartRecycelerView;
    private TextView dateText;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cartAdaptor = new CartAdaptor(HistoryActivity.this);
        cartRecycelerView = (RecyclerView)findViewById(R.id.cardRecuycelerView);
        cartRecycelerView.setAdapter(cartAdaptor);
        cartRecycelerView.setLayoutManager(new LinearLayoutManager(this));
        dateText = (TextView)findViewById(R.id.dateTextViewInHistory);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(date);
        String dateForFirebase = LocalDate.now().toString();
        dateText.setText(dateString);
        FirebaseDatabase.getCarts(HistoryActivity.this,cartAdaptor,dateForFirebase);
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
            SelectDate selectDate = new SelectDate(cartAdaptor,dateText);
            selectDate.show(getSupportFragmentManager(),"Choose Date");
        }
        return super.onOptionsItemSelected(item);
    }
}