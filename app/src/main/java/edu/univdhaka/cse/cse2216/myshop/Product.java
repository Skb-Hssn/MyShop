package edu.univdhaka.cse.cse2216.myshop;

import android.app.AlertDialog;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Product implements Serializable,Cloneable,Comparable<Product> {
    protected String name,companyName,unit;
    protected double availableQuantity,soldPrice;
    protected String firebaseProductId;

    public Product()
    {

    }

    public Product(String name, String companyName, String unit, double availableQuantity, double soldPrice)
    {
        this.name = name;
        this.companyName = companyName;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
        this.soldPrice = soldPrice;
    }

    public Product(String name, String companyName, String unit, double availableQuantity, double soldPrice,String firebaseProductId)
    {
        this.name = name;
        this.companyName = companyName;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
        this.soldPrice = soldPrice;
        this.firebaseProductId = firebaseProductId;
    }
    public void setFirebaseProductId(String firebaseProductId)
    {
        this.firebaseProductId = firebaseProductId;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUnit() {
        return unit;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public double getSoldPrice() {
        return soldPrice;
    }
    public String getFirebaseProductId()
    {
        return this.firebaseProductId;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        String details = name +" "+companyName+" "+firebaseProductId+" "+String.valueOf(soldPrice);
        return details;
    }

    @NonNull
    @NotNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(Product o) {
        if(this.name.toLowerCase().compareTo(o.getName().toLowerCase()) == 0 && this.companyName.toLowerCase().compareTo(o.getCompanyName().toLowerCase()) == 0 && Math.abs(this.soldPrice-o.getSoldPrice()) < .01)
        {
            return 0;
        }
        return 1;

    }
}
