package com.example.devyatkin.dishes;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable {
    private String name;    // dish name
    private String type;    // dish type {first, second, salad and etc}
    private String ingredient;
    private String cooking;
    private String imagePath;

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel source) {

            Dish dish = new Dish();
            dish.setName(source.readString());
            dish.setType(source.readString());
            dish.setIngredient(source.readString());
            dish.setCooking(source.readString());
            dish.setImagePath(source.readString());

            return dish;
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public Dish(){
        this.name = "";
        this.type = "";
        this.ingredient = "";
        this.cooking = "";
        this.imagePath = "";
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCooking() {
        return this.cooking;
    }

    public void setCooking(String cooking) {
        this.cooking = cooking;
    }

    public String getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(ingredient);
        dest.writeString(cooking);
        dest.writeString(imagePath);
    }
}
