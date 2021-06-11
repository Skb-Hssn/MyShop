package edu.univdhaka.cse.cse2216.myshop;

public class ShopKeeper {
    private String name,shopName,email;
    public ShopKeeper(String name,String shopName,String email)
    {
        this.name = name;
        this.shopName = shopName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getShopName() {
        return shopName;
    }

    public String getEmail() {
        return email;
    }
}
