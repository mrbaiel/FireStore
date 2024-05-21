package com.example.firestore.Model;

public class Users {
    private String name , password, phone, image;

    public Users()
    {
    }

    public Users(String name, String phone, String password, String image) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.image = image;
    }
    public String getImage(){
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

