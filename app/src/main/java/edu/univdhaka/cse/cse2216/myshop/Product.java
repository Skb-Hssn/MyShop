package edu.univdhaka.cse.cse2216.myshop;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Product {
    private String name,companyName,unit;
    private double availableQuantity,soldPrice;
    private String firebaseProductId;

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
}
