package com.tradesman.provider.model;

/**
 * Created by Elluminati Mohit on 1/21/2017.
 */

public class ProviderType {

    private String id;
    private String name;
    private String picture;
    private boolean isSelected;



    public String  getId() {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
