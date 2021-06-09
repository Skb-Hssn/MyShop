package edu.univdhaka.cse.cse2216.myshop;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Cart implements Serializable {
    private LocalDate date;
    private LocalTime time;
    double discount,paidAmount;
    int id;
    private ArrayList<Product> itemList;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Cart()
    {
        date = LocalDate.now();
        time = LocalTime.now();
        Log.d("noman",date.toString());
        Log.d("noman",time.toString());
        itemList = new ArrayList<>();
//        set id
    }
    public Cart(double discount)
    {
        date = LocalDate.now();
        time = LocalTime.now();
        Log.d("noman",date.toString());
        Log.d("noman",time.toString());
        itemList = new ArrayList<>();
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public ArrayList<Product> getItemList() {
        return itemList;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public void addItem(Product product)
    {
        itemList.add(product);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

}
