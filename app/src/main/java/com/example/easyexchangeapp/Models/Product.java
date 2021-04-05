package com.example.easyexchangeapp.Models;

import java.io.Serializable;

public class Product implements Serializable {
    String prodName;
    String prodDescription;
    String prodPrice;
    String imageUrl;
    String sellerName;
    String sellerID;
    String sellerEmail;
    String prodAddress;
    String soldStatus;
    String itemKey;

    public Product(String prodName, String prodDescription, String prodPrice, String imageUrl, String sellerName, String soldStatus, String itemKey,String sellerEmail, String prodAddress,String sellerID) {
        this.prodName = prodName;
        this.sellerID=sellerID;
        this.sellerEmail=sellerEmail;
        this.prodDescription = prodDescription;
        this.prodPrice = prodPrice;
        this.imageUrl = imageUrl;
        this.sellerName = sellerName;
        this.soldStatus = soldStatus;
        this.itemKey = itemKey;
        this.prodAddress = prodAddress;
    }

    public Product() {

    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSoldStatus() {
        return soldStatus;
    }

    public void setSoldStatus(String soldStatus) {
        this.soldStatus = soldStatus;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDescription() {
        return prodDescription;
    }

    public void setProdDescription(String prodDescription) {
        this.prodDescription = prodDescription;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdAddress() {
        return prodAddress;
    }

    public void setProdAddress(String prodAddress) {
        this.prodAddress = prodAddress;
    }
}
