package co.com.acktos.ubimoviltaxista.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acktos on 2/24/16.
 */
public class Car {

    private String id;
    private String plate;
    private String people;
    private String brand;
    @SerializedName("type_img")
    private String typeImg;
    private String model;
    private String type;

    public Car(String id, String plate, String people, String brand, String typeImg, String model, String type) {

        this.id = id;
        this.plate = plate;
        this.people = people;
        this.brand = brand;
        this.typeImg = typeImg;
        this.model = model;
        this.type = type;
    }

    @Override
    public String toString(){
        return getPlate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg;
    }

    public String getType() {
        return type;
    }

    public void setClassic(String classic) {
        this.type = classic;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
