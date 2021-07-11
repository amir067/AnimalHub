package com.example.animalhub.model;

import java.io.Serializable;
import java.util.UUID;

public class ModelAd implements Serializable {
    String adId,Title, Price, Description, Image,Id,Location,Phone,Address,Date,Category,SubCategory;
    boolean isApproved;
    public ModelAd() {
        adId= UUID.randomUUID().toString();
    }

    public boolean getApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public String toString() {
        return "ModelAd{" +
                "Title='" + Title + '\'' +
                ", Price='" + Price + '\'' +
                ", Description='" + Description + '\'' +
                ", Image='" + Image + '\'' +
                ", Id='" + Id + '\'' +
                ", Location='" + Location + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Address='" + Address + '\'' +
                ", Date='" + Date + '\'' +
                ", Category='" + Category + '\'' +
                ", SubCategory='" + SubCategory + '\'' +
                '}';
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
