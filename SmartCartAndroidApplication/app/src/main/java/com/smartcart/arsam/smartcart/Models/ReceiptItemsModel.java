package com.smartcart.arsam.smartcart.Models;

/**
 * Created by arsam on 05/05/2018.
 */

public class ReceiptItemsModel {
    int id;
    double total;
    String date;

    public ReceiptItemsModel(int id, double total, String date) {
        this.id = id;
        this.total = total;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
