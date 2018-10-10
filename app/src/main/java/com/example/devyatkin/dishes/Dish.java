package com.example.devyatkin.dishes;

import android.net.Uri;

public class Dish {
    private String name;    // dish name
    private String type;    // dish type {first, second, salad and etc}
    private String ingridient;
    private String cooking;
    private String path;

    public Dish(){
        this.name = "";
        this.type = "";
        this.ingridient = "";
        this.cooking = "";
        this.path = "";
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

    public String getIngridient() {
        return this.ingridient;
    }

    public void setIngridient(String ingridient) {
        this.ingridient = ingridient;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
