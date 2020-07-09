package com.ziko.isaac.easysalepost.Model;

public class EasySale {
    private String item_name;
    private String item_extended_description;
    private String picture_link;
    private int sale_nis;
    private int quantity;

    public EasySale(String iteName, String itemDescription, String imageUrl, int salePrice, int quantity) {
        this.item_name = iteName;
        this.item_extended_description = itemDescription;
        this.picture_link = imageUrl;
        this.sale_nis = salePrice;
        this.quantity = quantity;
    }

    public String getIteName() {
        return item_name;
    }
    public void setIteName(String iteName) {
        this.item_name = iteName;
    }
    public String getItemDescription() {
        return item_extended_description;
    }
    public void setItemDescription(String itemDescription) {
        this.item_extended_description = itemDescription;
    }
    public String getImageUrl() {
        return picture_link;
    }
    public void setImageUrl(String imageUrl) {
        this.picture_link = imageUrl;
    }
    public int getSalePrice() {
        return sale_nis;
    }
    public void setSalePrice(int salePrice) {
        this.sale_nis = salePrice;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "EasySale{" +
                "iteName='" + item_name + '\'' +
                ", itemDescription='" + item_extended_description + '\'' +
                ", imageUrl='" + picture_link + '\'' +
                ", salePrice=" + sale_nis +
                ", quantity=" + quantity +
                '}';
    }
}
