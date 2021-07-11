package com.example.animalhub.model;

public class Register_ModelClass {
    String name,email,Location,Date,phone,id;
    boolean isDelete;

    public Register_ModelClass(String name, String email, String location, String date, String phone, String id) {
        this.name = name;
        this.email = email;
        Location = location;
        Date = date;
        this.phone = phone;
        this.id = id;

    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Register_ModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
