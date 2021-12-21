package com.impinge.soul.models;

public class PersonalizedModel {
    public int personalizeID;
    private String personalizedValue;

    private boolean isSelected;

    public PersonalizedModel(int id, String value){
        this.personalizedValue = value;
        this.personalizeID = id;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPersonalizeID() {
        return personalizeID;
    }

    public void setPersonalizeID(int personalizeID) {
        this.personalizeID = personalizeID;
    }

    public String getPersonalizedValue() {
        return personalizedValue;
    }

    public void setPersonalizedValue(String personalizedValue) {
        this.personalizedValue = personalizedValue;
    }
}
