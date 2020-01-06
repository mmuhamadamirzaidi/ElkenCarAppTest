package com.mmuhamadamirzaidi.elkencarapptest.Model;

public class Car {

    private int id;
    private String manufacturer, name, price, plat;
    private byte[] image;

    public Car(int id, String manufacturer, String name, String price, String plat, byte[] image) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.name = name;
        this.price = price;
        this.plat = plat;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
