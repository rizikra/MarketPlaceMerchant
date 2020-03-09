package com.rizik.training.marketplaceapp.merchant.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.adapter.CategoriesAdapter;
import com.rizik.training.marketplaceapp.merchant.kelas.Category;
import com.rizik.training.marketplaceapp.merchant.kelas.ProductErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class AddProductsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RequestQueue requestQueue;
    private Spinner categoryDropDown;
    private ArrayList<Category> categories;
    private CategoriesAdapter categoriesAdapter;

    private Button btnPilih, btnTambah;
    private EditText edtProdName, edtProdQty, edtProdDesc, edtProdPrice;
    private ImageView imageView;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc, productQty, productPrice, categoryId, merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        btnPilih = findViewById(R.id.btn_choose_image);
        btnTambah = findViewById(R.id.btn_add_item);

        // ImageView for hold choosed image from intent
        imageView = findViewById(R.id.image_form_file);

        //Edit Text
        edtProdName = findViewById(R.id.input_item_name);
        edtProdPrice = findViewById(R.id.input_item_price);
        edtProdQty = findViewById(R.id.input_item_qty);
        edtProdDesc = findViewById(R.id.input_item_desc);

        // Spinner Categories
        categoryDropDown = findViewById(R.id.category_dropdown);
        // Array list for hold categories from server
        categories = new ArrayList<>();
        // set categories adapter to Spinner
        categoriesAdapter = new CategoriesAdapter();
        categoryDropDown.setAdapter(categoriesAdapter);
        categoryDropDown.setOnItemSelectedListener(this);
        // get all categories from server
        getAllCategories();

        //button choose image
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // harus dipastikan tidak ada data yang kosong...
                // lihat project VolumeApp untuk contoh validasi input tidak boleh kosong
                // ....
                // Berikut yang harus divalidasi :
                productName = edtProdName.getText().toString();
                productDesc = edtProdDesc.getText().toString();
                productPrice = edtProdPrice.getText().toString();
                productQty = edtProdQty.getText().toString();

                merchantId = "1"; //Sementara set ke 1

                //check String productImage ( sudah pilih gambar dari gallery atau belum)
                if (productImage == null) { // jika kosong,
                    productImage = null;     // isi dengan null
                }

                postProductToServer();
            }
        });

    }

    private void getAllCategories() {
        String url = "http://210.210.154.65:4444/api/categories";

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                Gson gson = new Gson();
                                Category category = gson.fromJson(data.getJSONObject(i).toString(), Category.class);
                                categories.add(category);
                            }

                            categoriesAdapter.addData(categories);
                            categoriesAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), String.valueOf(categoriesAdapter.getCount()), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        requestQueue.add(listCatReq);
    }

    // Override Spinner Method
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(),String.valueOf(categoriesAdapter.getItemId(position)),Toast.LENGTH_LONG).show();

        // set merchantId yang akan dikirim dari item yang dipilih dari Spinner Category, id didapat dari Object Category di adapter nya.
        this.categoryId = String.valueOf(categoriesAdapter.getItemId(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //kosong aja
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image",productImage);

                Glide.with(getApplicationContext()).load(bitmap).override(imageView.getWidth()).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convert image bitmap to string base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    private void postProductToServer() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://210.210.154.65:4444/api/products",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                ProductErrorResponse errorResponse = new Gson().fromJson(jsonObject.getString("message"), ProductErrorResponse.class);
                                if (errorResponse.getProductNameError().size() != 0) {
                                    if (errorResponse.getProductNameError().get(0) != null) {
                                        edtProdName.setError(errorResponse.getProductNameError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductNameError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (errorResponse.getProductPriceError().size() != 0) {
                                    if (errorResponse.getProductPriceError().get(0) != null) {
                                        edtProdPrice.setError(errorResponse.getProductPriceError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductPriceError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (errorResponse.getProductQtyError().size() != 0) {
                                    if (errorResponse.getProductQtyError().get(0) != null) {
                                        edtProdQty.setError(errorResponse.getProductQtyError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductQtyError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (errorResponse.getProductDescError().size() != 0) {
                                    if (errorResponse.getProductDescError().get(0) != null) {
                                        edtProdDesc.setError(errorResponse.getProductDescError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductDescError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            Log.i("response :", response);

                        }catch (JSONException ex) {
                            ex.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(), "Success Added product",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (error.networkResponse.statusCode == 400){
                            Toast.makeText(getApplicationContext(), String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                        }
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("productName", productName);
                params.put("productPrice", productPrice);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);

                if (productImage != null){
                    params.put("productImage", productImage);
                }

                params.put("categoryId", categoryId);
                params.put("merchantId", merchantId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("Content-Type", "json");
                params.put("Accept", "json");
                return super.getHeaders();
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}