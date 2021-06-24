package edu.univdhaka.cse.cse2216.myshop;

import java.util.Comparator;
import java.util.Map;

public class Item extends Product implements Comparator<Item> {
    double soldQuantity,totalPrice;
    public Item(Product product, double soldQuantity,double totalPrice)
    {
        super(product.name, product.companyName, product.unit, product.availableQuantity, product.soldPrice);
        this.soldQuantity = soldQuantity;
        this.totalPrice = totalPrice;

    }



    public double getSoldQuantity() {
        return soldQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public int compare(Item o1, Item o2) {
        if(o1.getName().compareTo(o2.getName()) == 0 && o1.getCompanyName().compareTo(o2.getCompanyName()) == 0 && Math.abs(o1.getSoldPrice()-o2.getSoldPrice()) < .00000001)
        {
            return 0;
        }
        return 1;
    }
}
