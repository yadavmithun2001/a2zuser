package com.cwservices.a2zproject.Models;

public class HouseNameModel {
    String name;
    int image;

    public HouseNameModel(String name, int image) {
        this.name = name;
        this.image = image;
    }
    public HouseNameModel(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
