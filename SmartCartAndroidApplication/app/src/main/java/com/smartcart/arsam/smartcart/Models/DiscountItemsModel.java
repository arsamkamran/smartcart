package com.smartcart.arsam.smartcart.Models;

/**
 * Created by Arsam on 17/01/2018.
 */

public class DiscountItemsModel {

    String name;
    String price;

    public DiscountItemsModel(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
