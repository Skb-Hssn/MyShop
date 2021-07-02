package edu.univdhaka.cse.cse2216.myshop.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.univdhaka.cse.cse2216.myshop.Authentication.Login;
import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.History.CartAdaptor;
import edu.univdhaka.cse.cse2216.myshop.Home.HomeActivity;
import edu.univdhaka.cse.cse2216.myshop.Item;
import edu.univdhaka.cse.cse2216.myshop.AddSale.ItemAdaptor;
import edu.univdhaka.cse.cse2216.myshop.Product;
import edu.univdhaka.cse.cse2216.myshop.ProductScreen.ProductAdaptor;
import edu.univdhaka.cse.cse2216.myshop.R;
import edu.univdhaka.cse.cse2216.myshop.ShopKeeper;

public class FirebaseDatabase {
    private static FirebaseAuth authentication;
    private static FirebaseFirestore myDatabase;
    private static ShopKeeper currentShopKeeper;
    public static ShopKeeper getCurrentShopKeeper()
    {
        return currentShopKeeper;
    }
    public static boolean isAlreadyLoggedIn()
    {
        authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        if(firebaseUser == null)
            return false;
        if(firebaseUser.isEmailVerified() == false)
            return false;
        Log.d("noman",firebaseUser.getEmail());
        return true;
    }
    public static void alreadyAnUser(ShopKeeper shopKeeper, String password, Context context)
    {
        authentication = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        authentication.fetchSignInMethodsForEmail(shopKeeper.getEmail())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<SignInMethodQueryResult> task) {
                        if(task.isSuccessful()) {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (isNewUser) {
                                signUp(shopKeeper, password, context, progressDialog);
                            } else {
                                progressDialog.dismiss();
                                TextView errorText = (TextView)((Activity) context).findViewById(R.id.sign_up_error_text);
                                errorText.setText(context.getResources().getString(R.string.haveAccount));
                            }
                        }
                        else if(task.getException() instanceof FirebaseNetworkException)
                        {

                            Log.d("noman","untracked problem");
                            Toast.makeText(context,"Connect to network",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
    public static void signUp(ShopKeeper shopKeeper, String password, Context context,ProgressDialog progressDialog)
    {

        authentication = FirebaseAuth.getInstance();
        authentication.createUserWithEmailAndPassword(shopKeeper.getEmail(),password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        TextView errorText = (TextView)((Activity) context).findViewById(R.id.sign_up_error_text);
                        errorText.setText("");
                        if(task.isSuccessful())
                        {
                            sendVerificationEmail(authentication,context);
                            storeUser(shopKeeper,context,progressDialog);

                        }
                        else if(task.getException() instanceof FirebaseNetworkException)
                        {

                            errorText.setText(context.getResources().getString(R.string.connectToNetwork));
                        }
                        else if(task.getException() instanceof FirebaseAuthWeakPasswordException)
                        {
                            errorText.setText("Password is weak");
                        }
                        else if(task.getException() instanceof FirebaseAuthException)
                        {
                            errorText.setText("Invalid mail");
                        }
                        else if(task.getException() instanceof FirebaseAuthEmailException)
                        {
                            errorText.setText("Email problem");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context,"Try again",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static void setCurrentShopKeeper(Context context,ProgressDialog progressDialog,String email)
    {
        authentication = FirebaseAuth.getInstance();
        myDatabase = FirebaseFirestore.getInstance();
        myDatabase.collection("users").whereEqualTo("email",email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String name,email,shopName;
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                name = documentSnapshot.getString("name");
                                email = documentSnapshot.getString("email");
                                shopName = documentSnapshot.getString("shopName");
                                currentShopKeeper = new ShopKeeper(name,shopName,email);
                                progressDialog.dismiss();
                                Intent intent = new Intent(context, HomeActivity.class);
                                ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                                context.startActivity(intent);
                                ((Activity)context).finish();



                            }


                        }
                        else if(task.getException() instanceof FirebaseNetworkException)
                        {
                            progressDialog.dismiss();
                            Log.d("noman","untracked problem");
                            Toast.makeText(context,"Connect to network",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
    public static void setCurrentShopKeeper(Context context)
    {

        authentication = FirebaseAuth.getInstance();
        myDatabase = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        FirebaseUser firebaseUser = authentication.getCurrentUser();
        assert firebaseUser != null;
        String email = firebaseUser.getEmail();
        myDatabase.collection("users").whereEqualTo("email",email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String name,email,shopName;
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                name = documentSnapshot.getString("name");
                                email = documentSnapshot.getString("email");
                                shopName = documentSnapshot.getString("shopName");
                                currentShopKeeper = new ShopKeeper(name,shopName,email);
                                progressDialog.dismiss();
                                Intent intent = new Intent(context, HomeActivity.class);
                                context.startActivity(intent);
                            }


                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        progressDialog.dismiss();
                    }
                });
    }
    public static void signIn(String email,String password,Context context,TextView errorText)
    {
        Log.d("noman","sign in ");
        errorText.setText("");
        ProgressDialog progressDialog = getProgressDialog(context);
        authentication = FirebaseAuth.getInstance();
        authentication.signOut();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
        {
            Log.d("noman","here");
            authentication.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = authentication.getCurrentUser();
                                if(user.isEmailVerified())
                                {
//                                    go to home page
                                    Log.d("noman","email is verified");
//                                    progressDialog.dismiss();
//                                    go to home
                                    setCurrentShopKeeper(context,progressDialog,email);

                                }
                                else
                                {
                                    Toast.makeText(context,"Verify your mail",Toast.LENGTH_SHORT).show();
                                    Log.d("noman","Verify your mail or resend code");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Resend verification email ? ")
                                            .setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    sendVerificationEmail(authentication,context);
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    progressDialog.dismiss();
                                    builder.setMessage("Your Email is not verified");
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            }
                            else if(task.getException() instanceof FirebaseNetworkException)
                            {
                                errorText.setText("Connect to Internet");
                            }
                            else if(task.getException() instanceof FirebaseAuthEmailException)
                            {
                                errorText.setText("Email problem");
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidUserException)
                            {
                                errorText.setText("Invalid user");
                            }
                            else if(task.getException() instanceof FirebaseAuthException)
                            {
                                errorText.setText("Wrong password");
                            }

                            Log.d("noman","untracked problem");

                            if(task.getException() != null) {
                                Log.d("Internet", task.getException().toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
//                            Toast.makeText(context,"Please Sign Up first",Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        }
                    });
        }
        else if(user.isEmailVerified())
        {
//            go to home page
            progressDialog.dismiss();
            Log.d("noman","go home");
        }
    }

    public static void signOut()
    {
        authentication = FirebaseAuth.getInstance();
        authentication.signOut();
    }
    private static void sendVerificationEmail(FirebaseAuth authentication,Context context)
    {

        FirebaseUser user = authentication.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Verify your Email",Toast.LENGTH_SHORT).show();
//                                            go to signIn page
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("noman","verification mail don't send");
                    }
                });

    }
    public static boolean isValidUser()
    {

        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
        {
            Log.d("noman","null");
            return false;
        }
        else
        {
            if(user.isEmailVerified())
            {
//

                return true;
            }
            else
            {
                //            go to Login
                Log.d("noman","not null");
                signOut();
                return false;
            }
        }
    }
    public static  ProgressDialog getProgressDialog(Context context)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dailog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return progressDialog;


    }


    public static void storeUser(ShopKeeper shopKeeper,Context context,ProgressDialog progressDialog)
    {
//        ProgressDialog progressDialog = getProgressDialog(context);
        authentication = FirebaseAuth.getInstance();
        myDatabase = FirebaseFirestore.getInstance();
        myDatabase.collection("users").add(shopKeeper)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
//                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Please verify your Email and sign In",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, Login.class);
                            context.startActivity(intent);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Can't save user",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

    }
    public static void addExistingProduct(Context context, Product product)
    {
//        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase = FirebaseFirestore.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        myDatabase.collection("products").document(user.getUid()).collection("products")
                .add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();
//                            getProducts(context,productAdaptor);

                        }
                        else
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
//                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void addProduct(Context context,Product product)
    {
//        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase = FirebaseFirestore.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        myDatabase.collection("products").document(user.getUid()).collection("products")
                .add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
//                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void getProducts(Context context, ProductAdaptor productAdaptor)
    {

        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase.collection("products").document(user.getUid()).collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            ArrayList<Product> products = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                String name,companyName,unit,firebaseId;
                                double availableQuantity,price;
                                name = document.get("name",String.class);
                                companyName = document.get("companyName",String.class);
                                unit = document.get("unit",String.class);
                                firebaseId = document.getId();
                                availableQuantity = document.get("availableQuantity",Double.class);
                                price = document.get("soldPrice",Double.class);
                                Product product = new Product(name,companyName,unit,availableQuantity,price,firebaseId);
                                products.add(product);

                            }
                            Log.d("noman","setting");
                            productAdaptor.setList(products);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public static void updateProduct(Context context,Product product)
    {
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();
//        ProgressDialog progressDialog = getProgressDialog(context);
        DocumentReference documentReference = myDatabase.collection("products").document(user.getUid()).collection("products").document(product.getFirebaseProductId());
        documentReference.set(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
//                            getProducts(context,productAdaptor);
                        }
                        else
                        {
//                            progressDialog.dismiss();
                            Log.d("noman","not success");
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
//                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void addCart(Context context, Cart cart)
    {
        Log.d("noman","cart");
        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase = FirebaseFirestore.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        myDatabase.collection("cart").document(user.getUid()).collection("cart")
                .add(cart)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Added cart",Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void getCarts(Context context, CartAdaptor cartAdaptor, String date)
    {
        Log.d("noman",date);
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase.collection("cart").document(user.getUid()).collection("cart")
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            ArrayList<Cart> carts = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("noman","noman");
                                Map<String,Object> map = document.getData();
                                String x = map.toString();
                                String time = (String) map.get("time");
                                String date = (String)map.get("date");
                                long id = (long)map.get("id");

                                double discount = (double)map.get("discount");
                                double paidAmount = (double)map.get("paidAmount");
                                ArrayList<HashMap<String,Object>> items = (ArrayList<HashMap<String,Object>>)map.get("itemList");
                                ArrayList<Item> cartItems = new ArrayList<>();

                                for(HashMap<String,Object> hashMap: items)
                                {
                                    String name = (String)hashMap.get("name");
                                    String companyName = (String)hashMap.get("companyName");
                                    String unit = (String)hashMap.get("unit");
                                    double quantity = (double)hashMap.get("availableQuantity");
                                    double price = (double)hashMap.get("soldPrice");
                                    double soldQuantity = (double)hashMap.get("soldQuantity");
                                    double totalCost = (double)hashMap.get("totalPrice");
                                    Product product = new Product(name,companyName,unit,quantity,price);
                                    Item newItem = new Item(product,soldQuantity,totalCost);
                                    cartItems.add(newItem);
                                }
                                Cart cart = new Cart((int)id,date,time,discount,paidAmount,cartItems);
                                carts.add(cart);
                            }


                            cartAdaptor.setList(carts);
                            Log.d("nomansalman",String.valueOf(carts.size()));
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void deleteProduct(Context context,Product product)
    {
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();
//        ProgressDialog progressDialog = getProgressDialog(context);
        DocumentReference documentReference = myDatabase.collection("products").document(user.getUid()).collection("products").document(product.getFirebaseProductId());
        documentReference
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
//                            getProducts(context,productAdaptor);
                        }
                        else
                        {
//                            progressDialog.dismiss();
                            Toast.makeText(context,"Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
//                        progressDialog.dismiss();
                        Toast.makeText(context,"Try again",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void updateJust(Product product)
    {
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();

        DocumentReference documentReference = myDatabase.collection("products").document(user.getUid()).collection("products").document(product.getFirebaseProductId());
        documentReference.set(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful())
                        {



                        }
                        else
                        {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });
    }
    public static void getProducts(Context context, ItemAdaptor itemAdaptor)
    {
        Log.d("noman","getProducts");
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null) {
            Log.d("noman","null");
            return;
        }
        myDatabase = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase.collection("products").document(user.getUid()).collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            ArrayList<Product> products = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("noman","calling");
                                String name,companyName,unit,firebaseId;
                                double availableQuantity,price;
                                name = document.get("name",String.class);
                                companyName = document.get("companyName",String.class);
                                unit = document.get("unit",String.class);
                                firebaseId = document.getId();
                                availableQuantity = document.get("availableQuantity",Double.class);
                                price = document.get("soldPrice", Double.class);
                                Product product = new Product(name,companyName,unit,availableQuantity,price,firebaseId);
                                products.add(product);

                            }
                            Log.d("noman","setting");
                            itemAdaptor.setList(products);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getDaySaleTotal(Context context, TextView textView)
    {
        String date = LocalDate.now().toString();
        authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        if(user == null)
            return;
        myDatabase = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = getProgressDialog(context);
        myDatabase.collection("cart").document(user.getUid()).collection("cart")
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            double total = 0;
                            ArrayList<Cart> carts = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Log.d("noman","noman");
                                Map<String,Object> map = document.getData();


                                double paidAmount = (double)map.get("paidAmount");
                                total += paidAmount;

                            }

                            textView.setText(String.format("%.2f", total));
                            Log.d("nomansalman",String.valueOf(carts.size()));
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void resetPassword(String email)
    {
        authentication = FirebaseAuth.getInstance();

        authentication.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });
    }
    public static boolean isEmailAddressValid(String email)
    {
        final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();

    }
}
