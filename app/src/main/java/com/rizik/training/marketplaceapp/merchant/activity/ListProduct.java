package com.rizik.training.marketplaceapp.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.adapter.AdapterProduct;
import com.rizik.training.marketplaceapp.merchant.kelas.ListProducts;
import com.rizik.training.marketplaceapp.merchant.kelas.Products;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListProduct extends AppCompatActivity {
    RecyclerView rvProduct;
    Button buttonAddProduct;
    final AdapterProduct adapterProduct = new AdapterProduct(this);
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        buttonAddProduct = findViewById(R.id.btn_tambahProduk);
        swr = findViewById(R.id.swipeRefresh);
        swr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VolleyLoad();
                swr.setRefreshing(false);

            }
        });

        inisialKomponen();
        VolleyLoad();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahProduk = new Intent(ListProduct.this, AddProductsActivity.class);
                startActivity(tambahProduk);
            }
        });

    }

    public void inisialKomponen(){
        rvProduct = findViewById(R.id.home_rv);
    }

    public void VolleyLoad(){
        requestQueue = Volley.newRequestQueue(this);
        String url = "http://210.210.154.65:4444/api/products";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            ListProducts listProducts = gson.fromJson(response.toString(), ListProducts.class);
                            ArrayList<Products> data = new ArrayList<>();
                            data.addAll(listProducts.getProducts());

                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ListProduct.this, 2);
                            rvProduct.setLayoutManager(layoutManager);
                            rvProduct.setAdapter(adapterProduct);
                            adapterProduct.addData(data);

                            Log.i("Response", response.toString());

                            Toast.makeText(ListProduct.this, String.valueOf(adapterProduct.getItemCount()), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListProduct.this, "Volley Error", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
    }
}
