package edu.univdhaka.cse.cse2216.myshop.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import edu.univdhaka.cse.cse2216.myshop.FirebaseDatabase;
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
            if(validAccount()) {
                Intent intent = new Intent(Login.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                errorText.setText(getResources().getText(R.string.sign_in_not_found_error));
            }
        });

        emailInput = findViewById(R.id.email_text_input);
        passwordInput = findViewById(R.id.password_text_input);
        errorText = findViewById(R.id.login_error_text);
    }

    /*
    * TODO : Check in firebase if it is a valid account
    * */
    public boolean validAccount() {
//        String email = emailInput.getEditText().getText().toString();
//        String password = passwordInput.getEditText().getText().toString();
//
//        if(email.equals("") || password.equals("")) {
//            return false;
//        }
//
////        if(email.equals("ABC") && password.equals("DEF")) return true;
//
////        return false;
////        sign in from firebase and go to home if email and password is correct
//        FirebaseDatabase.signIn(email,password,Login.this,errorText);
//        return true;
        return true;
    }
}