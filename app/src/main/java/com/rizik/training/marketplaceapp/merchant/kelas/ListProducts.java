package com.rizik.training.marketplaceapp.merchant.kelas;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListProducts {

    @SerializedName("data")
    private ArrayList<Products> products;

    public ListProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }
}
