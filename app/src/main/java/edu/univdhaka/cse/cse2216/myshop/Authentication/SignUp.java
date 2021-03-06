package edu.univdhaka.cse.cse2216.myshop.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.ShopKeeper;

public class SignUp extends AppCompatActivity {

    ImageView backArrow;
    Button signUpButton;
    TextInputLayout nameInput;
    TextInputLayout shopNameInput;
    TextInputLayout emailInput;
    TextInputLayout passwordInput;
    TextInputLayout retypedPasswordInput;
    TextView errorTextView;
    String errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp.super.onBackPressed();
            }
        });

        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(v -> {
            if (!validAccount()) {
                errorTextView.setText(errorText);
            }
        });

        nameInput = findViewById(R.id.sign_up_name_input);
        shopNameInput = findViewById(R.id.sign_up_shop_name_input);
        emailInput = findViewById(R.id.sign_up_email_input);
        passwordInput = findViewById(R.id.sign_up_password);
        retypedPasswordInput = findViewById(R.id.sign_up_retype_password);
        errorTextView = findViewById(R.id.sign_up_error_text);
    }


    public boolean validAccount() {
        errorText = "";
        String nameInputText = nameInput.getEditText().getText().toString();
        String shopNameInputText = shopNameInput.getEditText().getText().toString();
        String emailInputText = emailInput.getEditText().getText().toString();
        String passwordInputText = passwordInput.getEditText().getText().toString();
        String retypedPasswordInputText =retypedPasswordInput.getEditText().getText().toString();

        if(nameInputText.equals(""))  {
            errorText = "Name " + getResources().getString(R.string.filed_cant_be_empty);
            return false;
        }

        if(shopNameInputText.equals(""))  {
            errorText = "Shop name " + getResources().getString(R.string.filed_cant_be_empty);
            return false;
        }

        if(emailInputText.equals(""))  {
            errorText = "Email " + getResources().getString(R.string.filed_cant_be_empty);
            return false;
        }

        if(passwordInputText.equals(""))  {
            errorText = "Password " + getResources().getString(R.string.filed_cant_be_empty);
            return false;
        }

        if(retypedPasswordInputText.equals(""))  {
            errorText = "Retyped password " + getResources().getString(R.string.filed_cant_be_empty);
            return false;
        }

        if(!passwordInputText.equals(retypedPasswordInputText)) {
            errorText = "Retyped password doesn't match";
            return false;
        }

        if(!FirebaseDatabase.isEmailAddressValid(emailInputText)) {
            errorText = "Invalid Email";
            return false;
        }

        ShopKeeper shopKeeper = new ShopKeeper(nameInputText,shopNameInputText,emailInputText);
        FirebaseDatabase.alreadyAnUser(shopKeeper,passwordInputText,SignUp.this);

        return true;
    }
}