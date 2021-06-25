package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.annotation.SuppressLint;

import java.util.Locale;

public class AddSaleItem {
    private String itemName;
    private double itemQuantity;
    private double itemUnitPrice;
    private double itemTotalPrice;
    private String unit;
    private String itemId;


    AddSaleItem(String itemName, double itemQuantity, double itemUnitPrice) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.itemTotalPrice = itemQuantity * itemUnitPrice;
        this.unit = "pc";
    }

    AddSaleItem(String itemName, double itemQuantity, double itemUnitPrice, String unit) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.itemTotalPrice = itemQuantity * itemUnitPrice;
        this.unit = unit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(double itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemQuantityText() {
        return String.format(Locale.ENGLISH, "%.3f %s @ ৳ %.3f", itemQuantity, unit, itemUnitPrice);
    }

    public String getItemTotalPriceText() {
        return String.format("৳%.3f", itemTotalPrice);
    }
}
