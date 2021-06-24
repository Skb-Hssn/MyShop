package edu.univdhaka.cse.cse2216.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.univdhaka.cse.cse2216.myshop.AddSale.AddSale;
import edu.univdhaka.cse.cse2216.myshop.Authentication.Login;
import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.History.HistoryActivity;
import edu.univdhaka.cse.cse2216.myshop.Home.HomeActivity;
import edu.univdhaka.cse.cse2216.myshop.ProductScreen.ProductActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        FirebaseDatabase.signOut();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                Intent intent = new Intent(SplashScreen.this, Login.class);
//                startActivity(intent);
//                finish();

                if(FirebaseDatabase.isAlreadyLoggedIn())
                {
                    Log.d("noman","logged in");
                    FirebaseDatabase.setCurrentShopKeeper(SplashScreen.this);

//                    get data from firebase
//                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
//                    startActivity(intent);
                }
                else {
                    Log.d("noman","not logged in");

                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
//                    Intent intent = new Intent(SplashScreen.this, HistoryActivity.class);
//                    startActivity(intent);
                }


            }
        }, SPLASH_SCREEN_TIMER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}