package com.rizik.training.marketplaceapp.merchant.Utils;

import android.content.SharedPreferences;

import com.rizik.training.marketplaceapp.merchant.kelas.AccessToken;

public class TokenManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static TokenManager INSTANSIASI;

    public TokenManager(SharedPreferences pref){
        this.sharedPreferences = pref;
        this.editor = sharedPreferences.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences pref){
        if (INSTANSIASI == null){
            INSTANSIASI = new TokenManager(pref);
        }
        return INSTANSIASI;
    }

    public void simpanToken (AccessToken accessToken){
        editor.putString("TOKEN_TYPE", accessToken.getTokenType()).commit();
        editor.putString("ACCESS_TOKEN", accessToken.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", accessToken.getRefreshToken()).commit();
    }

    public void hapusToken(){
        editor.remove("TOKEN_TYPE").commit();
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
    }

    public AccessToken getToken(){
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(sharedPreferences.getString("TOKEN_TYPE",null));
        accessToken.setAccessToken(sharedPreferences.getString("ACCESS_TOKEN",null));
        accessToken.setAccessToken(sharedPreferences.getString("REFRESH_TOKEN",null));
        return accessToken;
    }
}
