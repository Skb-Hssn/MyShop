package edu.univdhaka.cse.cse2216.myshop;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Cart implements Serializable {
    private String date;
    private String time;
    double discount,paidAmount;
    int id;
    private ArrayList<Item> itemList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Cart()
    {
        date = LocalDate.now().toString();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm : ss a");
        time = dateTimeFormatter.format(LocalTime.now());
        itemList = new ArrayList<>();
        this.id = 3;
//        set id
    }
    public Cart(int id,String date,String time,double discount,double paidAmount,ArrayList<Item> itemList)
    {
        this.id = id;
        this.date = date;
        this.time = time;
        this.discount = discount;
        this.paidAmount = paidAmount;
        this.itemList = itemList;
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

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public void addItem(Item item)
    {
        itemList.add(item);
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }
}
