package com.rizik.training.marketplaceapp.merchant.kelas;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    private long productId;
    private String productName;
    private String productSlug;
    private int productPrice;
    private int productQty;
    private String productImage;
    private String productDesc;
    private Merchant merchant;
    private Category category;

    public Products(long productId, String productName, String productSlug, int productPrice, int productQty, String productImage, String productDesc, Merchant merchant, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productImage = productImage;
        this.productDesc = productDesc;
        this.merchant = merchant;
        this.category = category;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt((int) this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productSlug);
        dest.writeInt(this.productPrice);
        dest.writeInt(this.productQty);
        dest.writeString(this.productImage);
        dest.writeString(this.productDesc);
        dest.writeParcelable(this.merchant, flags);
        dest.writeParcelable(this.category, flags);
    }

    protected Products(Parcel in) {
        this.productId = in.readInt();
        this.productName = in.readString();
        this.productSlug = in.readString();
        this.productPrice = in.readInt();
        this.productQty = in.readInt();
        this.productImage = in.readString();
        this.productDesc = in.readString();
        this.merchant = in.readParcelable(Merchant.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel source) {
            return new Products(source);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };
}
