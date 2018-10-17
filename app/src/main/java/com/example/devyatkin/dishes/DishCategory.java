package com.example.devyatkin.dishes;

public class DishCategory {
    private String name;
    private int iconResource;

    public DishCategory(String name, int icon){
        this.name = name;
        this.iconResource = icon;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
