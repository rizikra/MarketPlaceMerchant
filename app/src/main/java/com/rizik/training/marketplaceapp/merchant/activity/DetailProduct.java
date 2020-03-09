package com.rizik.training.marketplaceapp.merchant.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.kelas.Products;

import androidx.appcompat.app.AppCompatActivity;

public class DetailProduct extends AppCompatActivity {

    private TextView tvProductID, tvProductName, tvProductSlug, tvQtyProduct, tvProductPrice;
    private TextView tvMerchantID, tvMerchantName, tvMerchantSlug;
    private TextView tvCategoryID, tvNameCategory;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Bundle bundle = getIntent().getExtras();
        String json = null;
        if (bundle != null) {
            json = bundle.getString("data");
        }

        Products products = new Gson().fromJson(json,Products.class);


        inisialisasi();
        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + products.getProductImage();

        Glide.with(this).load(url).into(image);


        tvProductID.setText("ID Produk          :   "+String.valueOf(products.getProductId()));
        tvProductName.setText("Nama Produk      :   "+products.getProductName());
        tvProductSlug.setText("Slug Produk      :   "+products.getProductSlug());
        tvQtyProduct.setText("Kuantitas Produk  :   "+String.valueOf(products.getProductQty()));
        tvProductPrice.setText("Harga Produk    :   "+String.valueOf(products.getProductPrice()));
        tvMerchantID.setText("ID Merchant       :   "+String.valueOf(products.getMerchant().getMerchantId()));
        tvMerchantName.setText("Nama Merchant   :   "+products.getMerchant().getMerchantName());
        tvMerchantSlug.setText("Slug Merchant   :   "+products.getMerchant().getMerchantSlug());
        tvCategoryID.setText("ID Kategori       :   "+String.valueOf(products.getCategory().getCategoryId()));
        tvNameCategory.setText("Nama Kategori   :   "+products.getCategory().getCategoryName());
    }

    public void inisialisasi(){
        tvProductID = findViewById(R.id.tv_des_id);
        tvProductName = findViewById(R.id.tv_des_nama);
        tvProductSlug = findViewById(R.id.tv_des_slug);
        tvQtyProduct = findViewById(R.id.tv_des_qty);
        tvProductPrice = findViewById(R.id.tv_price);
        tvMerchantID = findViewById(R.id.tv_des_mid);
        tvMerchantName = findViewById(R.id.tv_des_mname);
        tvMerchantSlug = findViewById(R.id.tv_des_mslug);
        tvCategoryID = findViewById(R.id.tv_des_cid);
        tvNameCategory = findViewById(R.id.tv_des_cname);
        image = findViewById(R.id.img_des);

    }
}
