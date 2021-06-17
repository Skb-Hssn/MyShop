package edu.univdhaka.cse.cse2216.myshop.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        seeUser();

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

        /*
        * NavigationView
        * */
        homeNavigation.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                homeToolbar, R.string.open, R.string.close);
        homeDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        homeToolbar.setNavigationIcon(R.drawable.menu_icon_white);
        homeToolbar.setTitleTextColor(getResources().getColor(R.color.primary_color));

        homeNavigation.setNavigationItemSelectedListener(this);


        /*
        * Set up navigation header text
        * */
        setHomeNavigationHeaderStoreName();
        setHomeNavigationHeaderEmail();

        /*
        * Set up today's information
        **/
        setTodayDate();
        setTodayTotalSale();


        productItem.setOnMenuItemClickListener(item12 -> {
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

    /*
    * TODO : Open product Activity.
    * */
    public void openProductActivity() {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        startActivity(intent);
    }

    /*
    * TODO: Open History Activity
    * */
    public void openHistoryActivity() {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    /*
    * TODO: Get the store name from FIREBASE
    * */
    public void setHomeNavigationHeaderStoreName() {
        String name = FirebaseDatabase.getCurrentShopKeeper().getShopName();
        homeNavigationHeaderStoreName.setText(name);
    }

    /*
     * TODO: Get the email from FIREBASE
    * */
    public void setHomeNavigationHeaderEmail() {
        String email = FirebaseDatabase.getCurrentShopKeeper().getEmail();
        homeNavigationHeaderEmail.setText(email);
    }

    /*
    * TODO: set sign out state in FIREBASE
    * */
    public void signOut() {
        FirebaseDatabase.signOut();
        Intent intent = new Intent(HomeActivity.this, Login.class);
        startActivity(intent);
        homeDrawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }

    /*
    *   TODO: Get Today's date every refresh time
    **/
    @SuppressLint("SimpleDateFormat")
    public void setTodayDate() {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        dateToday.setText(dateFormat.format(calendar.getTime()));
    }

    /*
    * TODO: Get today's total amount of sale From FIREBASE
    * */
    public void setTodayTotalSale() {
//        FirebaseDatabase.getDaySaleTotal();
        String totalSale = "550";
        totalAmountToday.setText(totalSale);
    }


    /*
    * TODO : Make Add Sale Activity.
    * */
    public void addSale() {

    }
//    debug purpose noman
    private void seeUser()
    {
        ShopKeeper shopKeeper = FirebaseDatabase.getCurrentShopKeeper();
        Log.d("noman",shopKeeper.getName());
        Log.d("noman",shopKeeper.getEmail());
        Log.d("noman",shopKeeper.getShopName());
    }
}