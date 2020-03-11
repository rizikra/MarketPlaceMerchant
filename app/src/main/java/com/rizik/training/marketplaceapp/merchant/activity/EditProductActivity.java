package com.rizik.training.marketplaceapp.merchant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.adapter.CategoriesAdapter;
import com.rizik.training.marketplaceapp.merchant.kelas.Category;
import com.rizik.training.marketplaceapp.merchant.kelas.ProductErrorResponse;
import com.rizik.training.marketplaceapp.merchant.kelas.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtProdName, edtProdPrice, edtProdDesc, edtProdQty, edtIdMerchant, edtIdCategory;
    private Button btnPilihGambar, btnTambahProduk;
    private ImageView imageView;

    private RequestQueue requestQueue;

    private String slug;
    private Products products;

//    private ArrayList<Category> categories;
//    private CategoriesAdapter categoriesAdapter;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc, productQty, productPrice, categoryId, merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        slug = bundle.getString("slug");

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Edit Text
        edtProdName = findViewById(R.id.input_item_name);
        edtProdPrice = findViewById(R.id.input_item_price);
        edtProdDesc = findViewById(R.id.input_item_desc);
        edtProdQty = findViewById(R.id.input_item_qty);

        edtIdMerchant = findViewById(R.id.input_id_merchant);
        edtIdCategory = findViewById(R.id.input_id_category);

        btnPilihGambar = findViewById(R.id.btn_choose_image);
        btnTambahProduk = findViewById(R.id.btn_add_item);

        // ImageView for hold choosed image from intent
        imageView = findViewById(R.id.image_form_file);

        getDataSebelumnya();

        //button choose image
        btnPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // harus dipastikan tidak ada data yang kosong...
                // lihat project VolumeApp untuk contoh validasi input tidak boleh kosong
                // ....
                // Berikut yang harus divalidasi :
                productName = validasiEdt(edtProdName);
                productDesc = validasiEdt(edtProdDesc);
                productPrice = validasiEdt(edtProdPrice);
                productQty = validasiEdt(edtProdQty);
                categoryId = validasiEdt(edtIdCategory);
                merchantId = validasiEdt(edtIdMerchant);

                merchantId = "1"; //Sementara set ke 1

                //check String productImage ( sudah pilih gambar dari gallery atau belum)
                if (productImage == null) { // jika kosong,
                    productImage = null;     // isi dengan null
                }

                VolleyLoad();

                Intent intent = new Intent(EditProductActivity.this, ListProduct.class);
                startActivity(intent);
            }
        });

    }

    public String validasiEdt(EditText edt) {
        String text = null;
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Data Harus Diisi!");
        } else {
            text = edt.getText().toString();
        }
        return text;
    }

    private void VolleyLoad() {
        String url = "http://210.210.154.65:4444/api/product/" + products.getProductId() + "/update";
        requestQueue = Volley.newRequestQueue(this);

//        http://{baseurl}/api/product/{id}/update
//        Method : PUT

        StringRequest listCatReq = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("response :", response);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(EditProductActivity.this, "data telah terupdate", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("productName", productName);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);

                if (productImage != null) {
                    params.put("productImage", productImage);
                }

                params.put("productPrice", productPrice);
                params.put("categoryId", categoryId);
                params.put("merchantId", merchantId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("Content-type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        listCatReq.setRetryPolicy(policy);
        requestQueue.add(listCatReq);
    }

    private void getDataSebelumnya() {

        String url = "http://210.210.154.65:4444/api/product/" + slug;

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("data");
                            products = gson.fromJson(jsonObject.toString(), Products.class);

                            edtProdName.setText(products.getProductName());
                            edtProdDesc.setText(products.getProductDesc());
                            edtProdQty.setText(String.valueOf(products.getProductQty()));
                            Glide.with(getApplicationContext()).load(products.getProductImage()).into(imageView);
                            edtProdPrice.setText(String.valueOf(products.getProductPrice()));
                            edtIdMerchant.setText(String.valueOf(products.getMerchant().getMerchantId()));
                            edtIdCategory.setText(String.valueOf(products.getCategory().getCategoryId()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProductActivity.this, "Data tidak terbaca", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
    }

    // get image from implicit intent
    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    // get result image from intent above
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image", productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(imageView.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
                System.out.println("image : " + productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convert image bitmap to string base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}