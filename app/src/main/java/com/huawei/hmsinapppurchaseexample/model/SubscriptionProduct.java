package com.huawei.hmsinapppurchaseexample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */
public class SubscriptionProduct {
    @SerializedName("userId")
    private String  userId;
    @SerializedName("purchaseToken")
    private String  purchaseToken;
    @SerializedName("subscriptionId")
    private String  subscriptionId;
    @SerializedName("subIsvalid")
    private boolean  subIsvalid;
    @SerializedName("productId")
    private String  productId;
    @SerializedName("productName")
    private String  productName;
    @SerializedName("purchaseState")
    private int  purchaseState;
    @SerializedName("currency")
    private String  currency;
    @SerializedName("price")
    private int  price;
    @SerializedName("purchaseTime")
    private long  purchaseTime;
    @SerializedName("country")
    private String  country;
    @SerializedName("expirationDate")
    private long  expirationDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public boolean isSubIsvalid() {
        return subIsvalid;
    }

    public void setSubIsvalid(boolean subIsvalid) {
        this.subIsvalid = subIsvalid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }
}
