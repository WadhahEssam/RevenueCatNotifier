package com.example.revenuecatnotifier;

public class Transaction {
    private String uid;
    private String sku;
    private String revenue;
    private String purchased;
    private String expiration;
    private Boolean isRenewal;

    public Transaction(String uid, String sku, String revenue, String purchased, String expiration, Boolean renewal) {
        this.uid = uid;
        this.sku = sku;
        this.revenue = revenue;
        this.purchased = purchased;
        this.expiration = expiration;
        this.isRenewal = renewal;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Boolean getIsRenewal() {
        return isRenewal;
    }

    public void setTrail(Boolean renewal) {
        isRenewal = renewal;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "uid='" + uid + '\'' +
                ", sku='" + sku + '\'' +
                ", revenue='" + revenue + '\'' +
                ", purchased='" + purchased + '\'' +
                ", expiration='" + expiration + '\'' +
                ", renewal=" + isRenewal +
                '}';
    }
}
