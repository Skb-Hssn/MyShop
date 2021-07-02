package edu.univdhaka.cse.cse2216.myshop.Home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.univdhaka.cse.cse2216.myshop.AddSale.AddSale;
import edu.univdhaka.cse.cse2216.myshop.Authentication.Login;

import edu.univdhaka.cse.cse2216.myshop.History.CartDetailsActivity;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.History.HistoryActivity;
import edu.univdhaka.cse.cse2216.myshop.ProductScreen.ProductActivity;

import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.ShopKeeper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout homeDrawerLayout;
    NavigationView homeNavigation;
    Toolbar homeToolbar;
    MenuItem productItem;
    MenuItem historyItem;
    TextView homeNavigationHeaderStoreName;
    TextView homeNavigationHeaderEmail;
    TextView totalAmountToday;
    TextView dateToday;
    Button signOutButton;
    Button addSaleButton;
    private ShopKeeper shopKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        shopKeeper = FirebaseDatabase.getCurrentShopKeeper();

        homeDrawerLayout = findViewById(R.id.home_drawer_layout);
        homeNavigation = findViewById(R.id.home_navigation);
        homeToolbar = findViewById(R.id.home_toolbar);

        productItem = homeNavigation.getMenu().findItem(R.id.home_nav_product);
        historyItem = homeNavigation.getMenu().findItem(R.id.home_nav_history);

        homeNavigationHeaderStoreName = homeNavigation.getHeaderView(0).findViewById(R.id.home_nav_header_store_name);
        homeNavigationHeaderEmail = homeNavigation.getHeaderView(0).findViewById(R.id.home_nav_header_email);
        signOutButton = homeNavigation.findViewById(R.id.home_nav_sign_out_button);

        addSaleButton = findViewById(R.id.add_sale_button);
        totalAmountToday = findViewById(R.id.today_total_amount);
        dateToday = findViewById(R.id.date_today);

        setSupportActionBar(homeToolbar);

        homeNavigation.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                homeToolbar, R.string.open, R.string.close);
        homeDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        homeToolbar.setNavigationIcon(R.drawable.menu_icon_30);
        homeToolbar.setTitleTextColor(getResources().getColor(R.color.primary_color));

        homeNavigation.setNavigationItemSelectedListener(this);

        setHomeNavigationHeaderStoreName();
        setHomeNavigationHeaderEmail();

        setTodayDate();
        setTodayTotalSale();


        productItem.setOnMenuItemClickListener(v -> {
            openProductActivity();
            homeDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        historyItem.setOnMenuItemClickListener(v ->{
            openHistoryActivity();
            homeDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        signOutButton.setOnClickListener(v -> {
            signOut();
        });

        addSaleButton.setOnClickListener(v -> {
            addSale();
        });
    }

    @Override
    public void onBackPressed() {
        if(homeDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return true;
    }

    public void openProductActivity() {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        startActivity(intent);
    }


    public void openHistoryActivity() {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        startActivity(intent);
    }


    public void setHomeNavigationHeaderStoreName() {
        homeNavigationHeaderStoreName.setText(shopKeeper.getShopName());
    }


    public void setHomeNavigationHeaderEmail() {
        homeNavigationHeaderEmail.setText(shopKeeper.getEmail());
    }


    public void signOut() {
        FirebaseDatabase.signOut();
        Intent intent = new Intent(HomeActivity.this, Login.class);
        startActivity(intent);

        homeDrawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }


    @SuppressLint("SimpleDateFormat")
    public void setTodayDate() {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        dateToday.setText(dateFormat.format(calendar.getTime()));
    }


    public void setTodayTotalSale() {

    }


    public void addSale() {
        Intent intent = new Intent(HomeActivity.this, AddSale.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getDaySaleTotal(HomeActivity.this, totalAmountToday);
    }
}