package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryPojoModel implements Serializable {
    ArrayList<CategoryModel> category;

    public ArrayList<CategoryModel> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoryModel> category) {
        this.category = category;
    }
}
