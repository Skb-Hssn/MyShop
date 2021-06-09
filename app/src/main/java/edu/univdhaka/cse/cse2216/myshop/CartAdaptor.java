package edu.univdhaka.cse.cse2216.myshop;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartViewHolder> {
    TextView timeText;
    TextView totalText;
    TextView discountText;
    TextView paidText;
    private Context context;

    private ArrayList<Cart> cartInList;
    private ArrayList<Cart> carts;
    public CartAdaptor(Context context)
    {
        this.context = context;
        cartInList = new ArrayList<>();
    }
    @NonNull
    @NotNull
    @Override
    public CartAdaptor.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_cart_display,parent,false);
        return new CartViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdaptor.CartViewHolder holder, int position) {
        timeText = (TextView)holder.itemView.findViewById(R.id.timeTextView);
        totalText = (TextView)holder.itemView.findViewById(R.id.totalTextView);
        discountText = (TextView)holder.itemView.findViewById(R.id.discountTextView);
        paidText = (TextView)holder.itemView.findViewById(R.id.paidTextView);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm : ss a");
        timeText.setText(dateTimeFormatter.format(cartInList.get(position).getTime()));
        totalText.setText(String.valueOf(cartInList.get(position).getDiscount()+cartInList.get(position).getPaidAmount()));
        discountText.setText(String.valueOf(cartInList.get(position).getDiscount()));
        paidText.setText(String.valueOf(cartInList.get(position).getPaidAmount()));
        ConstraintLayout container = (ConstraintLayout)holder.itemView.findViewById(R.id.cartContainer);
        Log.d("noman","time");
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailActivity(cartInList.get(position));
            }
        });

    }
    public void goToDetailActivity(Cart cart)
    {
        Intent intent = new Intent(context,CartDetailsActivity.class);
        intent.putExtra("cartObject",cart);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return cartInList.size();
    }
    public void set(ArrayList<Cart> carts)
    {
        this.cartInList = carts;
        this.carts = carts;
        Log.d("noman",String.valueOf(cartInList.size()));
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {
        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            timeText = (TextView)itemView.findViewById(R.id.timeTextView);
            totalText = (TextView)itemView.findViewById(R.id.totalTextView);
            discountText = (TextView)itemView.findViewById(R.id.discountTextView);
            paidText = (TextView)itemView.findViewById(R.id.paidTextView);
        }
    }
}
