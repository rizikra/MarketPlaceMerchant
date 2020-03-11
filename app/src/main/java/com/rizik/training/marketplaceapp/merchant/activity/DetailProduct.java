package com.rizik.training.marketplaceapp.merchant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.kelas.Products;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailProduct extends AppCompatActivity {

    private TextView tvProductID, tvProductName, tvProductSlug, tvQtyProduct, tvProductPrice;
    private Button btnEdit, btnDelete;
    private TextView tvMerchantID, tvMerchantName, tvMerchantSlug;
    private TextView tvCategoryID, tvNameCategory;
    private ImageView image;
    private Products products = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Bundle bundle = getIntent().getExtras();
        String json = null;
        if (bundle != null) {
            json = bundle.getString("data");
        }

        products = new Gson().fromJson(json,Products.class);


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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailProduct.this);
                alert.setTitle("Delete Product");
                alert.setMessage("Apakah Produk Akan Dihapus ? ");
                alert.setNegativeButton("Tidak", null);
                alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        deleteProduct();

                        Intent intent = new Intent(DetailProduct.this, ListProduct.class);
                        startActivity(intent);
                    }
                });
                alert.create().show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProduct.this, EditProductActivity.class);
                intent.putExtra("slug", products.getProductSlug());
                startActivity(intent);
            }
        });
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

        btnEdit = findViewById(R.id.editProduct);
        btnDelete = findViewById(R.id.deleteProduct);

    }
    public void deleteProduct(){

        String url = "http://210.210.154.65:4444/api/product/"+products.getProductId()+"/delete";

        RequestQueue con = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DetailProduct.this, "Data Telah Terhapus", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailProduct.this, "Data Belum Terhapus , Cek Koneksi Internet", Toast.LENGTH_SHORT).show();
                    }
                });
        con.add(req);
    }
}