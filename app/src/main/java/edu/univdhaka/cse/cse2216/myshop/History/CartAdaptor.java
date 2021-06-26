package edu.univdhaka.cse.cse2216.myshop.History;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.univdhaka.cse.cse2216.myshop.Cart;
import edu.univdhaka.cse.cse2216.myshop.History.HistoryActivity;
import edu.univdhaka.cse.cse2216.myshop.R;

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
    public void setList(ArrayList<Cart> carts)
    {
        this.cartInList = carts;
        this.carts = carts;
        Collections.sort(this.carts);
        notifyDataSetChanged();
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

        timeText.setText(cartInList.get(position).getTime());
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
        Intent intent = new Intent(context, CartDetailsActivity.class);
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
