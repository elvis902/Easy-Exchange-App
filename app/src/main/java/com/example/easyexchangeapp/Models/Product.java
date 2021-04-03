package com.example.easyexchangeapp.Models;

public class Product {
    String prodName;
    String prodDescription;
    String prodPrice;
    String imageUrl;
    String sellerName;
    String sellerEmail;
    String soldStatus;
    String itemKey;

    public Product(String prodName, String prodDescription, String prodPrice, String imageUrl, String sellerName, String soldStatus, String itemKey,String sellerEmail) {
        this.prodName = prodName;
        this.sellerEmail=sellerEmail;
        this.prodDescription = prodDescription;
        this.prodPrice = prodPrice;
        this.imageUrl = imageUrl;
        this.sellerName = sellerName;
        this.soldStatus = soldStatus;
        this.itemKey = itemKey;
    }

    public Product() {
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

}
