package edu.univdhaka.cse.cse2216.myshop;

import java.util.Comparator;
import java.util.Map;

public class Item extends Product{
    double soldQuantity,totalPrice;
    public Item(Product product, double soldQuantity,double totalPrice)
    {
        super(product.name, product.companyName, product.unit, product.availableQuantity, product.soldPrice,product.firebaseProductId);
        this.soldQuantity = soldQuantity;
        this.totalPrice = totalPrice;

    }



    public double getSoldQuantity() {
        return soldQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }


}
