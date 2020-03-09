package com.rizik.training.marketplaceapp.merchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rizik.training.marketplaceapp.merchant.R;
import com.rizik.training.marketplaceapp.merchant.Utils.TokenManager;
import com.rizik.training.marketplaceapp.merchant.kelas.AccessToken;
import com.rizik.training.marketplaceapp.merchant.kelas.RegisterErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class LoginMerchantActivity extends AppCompatActivity {
    TextView textViewDaftar;
    EditText editTextNamaDepan, editTextNamaBelakang, editTextEmailLogin, editTextPasswordLogin, editTextConfirmPassword;
    EditText editTextMerchantName;
    Button buttonLogin;

    RequestQueue requestQueue;
    //cara kedua gak usah buat RequestQueue karena sudah ada VolleyService

    AccessToken accessToken;

    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";

    String firstName, lastName, email, password, confirmPassword, merchantName;
    int isMerchant = 1; // set 1 for true, set 1 in merchant app, and 0 in customer app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_merchant);
        textViewDaftar = findViewById(R.id.klik_daftar);
        editTextEmailLogin = findViewById(R.id.edt_emailLogin);
        editTextPasswordLogin = findViewById(R.id.edt_kataSandiLogin);
        buttonLogin = findViewById(R.id.btn_loginApp);

        requestQueue = Volley.newRequestQueue(this);

        textViewDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Register = new Intent(LoginMerchantActivity.this, DaftarMerchantActivity.class);
                startActivity(Register);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput() == true){
                    postDataRegister();
                }
            }
        });
    }

    private void postDataRegister(){

        email = editTextEmailLogin.getText().toString();
        password = editTextPasswordLogin.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/login";
        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // do whatever u want with response
                        accessToken = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).simpanToken(accessToken);
                        Toast.makeText(getApplicationContext(),"Berhasil Login :)", Toast.LENGTH_LONG).show();
                        Intent PindahApp = new Intent(LoginMerchantActivity.this, ListProduct.class);
                        startActivity(PindahApp);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf( error.networkResponse.statusCode );
                        Toast.makeText(getApplicationContext(), statusCode, Toast.LENGTH_LONG).show();

                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);

                            RegisterErrorResponse errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(),RegisterErrorResponse.class);


                            if(errorResponse.getEmailError().size() > 0){
                                if(errorResponse.getEmailError().get(0) != null){
                                    editTextEmailLogin.setError(errorResponse.getEmailError().get(0));
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new Hashtable<>();

                headers.put("Accept","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put(EMAIL,email);
                params.put(PASSWORD,password);


                return params;
            }
        };

        requestQueue.add(registerReq);
        //VolleyService.getInstance(getApplicationContext()).addToRequestQueue(registerReq);

    }
    private boolean isValidInput(){

        boolean isValid = true;



        if(editTextEmailLogin.getText().toString().isEmpty()){
            editTextEmailLogin.setError("email name cannot be empty");
            isValid = false;
        }else if(!editTextEmailLogin.getText().toString().contains("@")){
            editTextEmailLogin.setError("must be a valid email");
            isValid = false;
        }

        if(editTextPasswordLogin.getText().toString().isEmpty()){
            editTextPasswordLogin.setError("Password cannot be empty");
            isValid = false;
        }
        else if(editTextPasswordLogin.getText().toString().length() < 8){
            editTextPasswordLogin.setError("Password must be 8 or more character");
            isValid = false;
        }


        return isValid;
    }
}

