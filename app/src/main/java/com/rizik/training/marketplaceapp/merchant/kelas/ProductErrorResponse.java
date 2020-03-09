package com.rizik.training.marketplaceapp.merchant.kelas;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.List;

public class ProductErrorResponse {
    @SerializedName("productName")
    List<String> productNameError = new ArrayList<>();
    @SerializedName("productQty")
    List<String> productQtyError = new ArrayList<>();
    @SerializedName("productPrice")
    List<String> productPriceError = new ArrayList<>();
    @SerializedName("productDesc")
    List<String> productDescError = new ArrayList<>();


    public List<String> getProductNameError() {
        return productNameError;
    }

    public List<String> getProductQtyError() {
        return productQtyError;
    }

    public List<String> getProductPriceError() {
        return productPriceError;
    }

    public List<String> getProductDescError() {
        return productDescError;
    }
}