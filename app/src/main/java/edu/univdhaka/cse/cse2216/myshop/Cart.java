package edu.univdhaka.cse.cse2216.myshop;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Cart implements Serializable,Comparable<Cart> {
    private String date;
    private String time;
    private double discount = 0;
    private double paidAmount;
    private double total;

    int id;
    private ArrayList<Item> itemList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Cart()
    {
        date = LocalDate.now().toString();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm : ss a");
        time = dateTimeFormatter.format(LocalTime.now()).toUpperCase();
        itemList = new ArrayList<>();

        this.id = (int) (new Date()).getTime();
        paidAmount = 0;
        total = 0;
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
        this.total = this.paidAmount + discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        paidAmount = total - this.discount;

    }
    public void addItem(Item item)
    {
        this.total += item.totalPrice;
        this.paidAmount += item.totalPrice;

        itemList.add(item);
    }

    public void removeItem(int position) {
        itemList.remove(position);
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public double getTotal()
    {
        return this.total;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public void setTotal (double total) {
        this.total = total;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Cart o) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm : ss a");

        LocalTime time1;
        LocalTime time2;
        try {
            time1 = LocalTime.parse(this.time.toLowerCase(),dateTimeFormatter);
            time2 = LocalTime.parse(o.time.toLowerCase(),dateTimeFormatter);
            return time2.compareTo(time1);
        }
        catch (DateTimeParseException e)
        {
            time1 = LocalTime.parse(this.time,dateTimeFormatter);
            time2 = LocalTime.parse(o.time,dateTimeFormatter);

            return time2.compareTo(time1);
        }
    }
}
