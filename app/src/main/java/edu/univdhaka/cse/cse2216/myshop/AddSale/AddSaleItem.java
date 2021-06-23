package edu.univdhaka.cse.cse2216.myshop.AddSale;

public class AddSaleItem {
    private String itemName;
    private int itemQuantity;
    private int itemUnitPrice;
    private int itemTotalPrice;
    private String unit;

    AddSaleItem(String itemName, int itemQuantity, int itemUnitPrice) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.itemTotalPrice = itemQuantity * itemUnitPrice;
        this.unit = "pc";
    }

    AddSaleItem(String itemName, int itemQuantity, int itemUnitPrice, String unit) {
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

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(int itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public int getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemQuantityText() {
        return itemQuantity + " " + unit + " @ ৳" + itemUnitPrice;
    }

    public String getItemTotalPriceText() {
        return "৳" + Integer.toString(itemTotalPrice);
    }
}
