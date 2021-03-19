package com.example.easyexchangeapp.Models;

public class Product {
    String prodName;
    String prodDescription;
    String prodPrice;
    String sellerAddress;

    public Product( String prodName, String prodDescription, String prodPrice/*,String sellerAddress*/) {
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.prodPrice = prodPrice;
        //this.sellerAddress = sellerAddress;
    }

    public Product() {
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

    /*public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }*/
}
