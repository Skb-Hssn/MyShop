package edu.univdhaka.cse.cse2216.myshop.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.Home.HomeActivity;
import edu.univdhaka.cse.cse2216.myshop.R;

public class Login extends AppCompatActivity {

    public static Activity signInActivity;

    Button continueToSignUp;
    Button signInButton;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInActivity = this;

        continueToSignUp = findViewById(R.id.continue_to_sign_up_button);
        continueToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> {
        validAccount();
//            if(validAccount()) {
//                Intent intent = new Intent(Login.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                errorText.setText(getResources().getText(R.string.sign_in_not_found_error));
//            }
        });

        emailInput = findViewById(R.id.email_text_input);
        passwordInput = findViewById(R.id.password_text_input);
        errorText = findViewById(R.id.login_error_text);

        if(isInternetWorking()) {
            Log.d("Internet", "Internet Working");
        } else {
            Log.d("Internet", "Internet not Working");
        }
    }

    /*
    * TODO : Check in firebase if it is a valid account
    * */

    public void validAccount() {



        String email = emailInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();

        if(email.equals("") || password.equals("")) {
            errorText.setText("Field can't be empty");
            return;
        }
        if(!FirebaseDatabase.isEmailAddressValid(email))
        {
            Log.d("noman","Invalid");
            errorText.setText("Invalid Email");
            return;
        }

//        if(email.equals("ABC") && password.equals("DEF")) return true;

//        return false;
//        sign in from firebase and go to home if email and password is correct
        FirebaseDatabase.signIn(email,password,Login.this,errorText);

    }

    public boolean isInternetWorking() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}