package com.rizik.training.marketplaceapp.merchant.kelas;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RegisterErrorResponse {
    @SerializedName("email")
    List<String> emailError = new ArrayList<>();

    public List<String> getEmailError() {
        return emailError;
    }
}
