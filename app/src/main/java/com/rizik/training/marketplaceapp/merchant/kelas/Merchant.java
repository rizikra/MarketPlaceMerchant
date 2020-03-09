package com.rizik.training.marketplaceapp.merchant.kelas;

import android.os.Parcel;
import android.os.Parcelable;

public class Merchant implements Parcelable {
    private long merchantId;
    private String merchantName;
    private String merchantSlug;

    public Merchant(long merchantId, String merchantName, String merchantSlug) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantSlug = merchantSlug;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantSlug() {
        return merchantSlug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt((int) this.merchantId);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantSlug);
    }

    protected Merchant(Parcel in) {
        this.merchantId = in.readInt();
        this.merchantName = in.readString();
        this.merchantSlug = in.readString();
    }

    public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel source) {
            return new Merchant(source);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };
}
