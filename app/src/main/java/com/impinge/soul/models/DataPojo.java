package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPojo implements Serializable {

    public MeditationPojo meditation;
    public ArrayList<MostPopularPojo> most_popular_songs;
    public ArrayList<songsPojo> songs;
    public TodaySpecialPojo today_special;
    public ArrayList<CategoryPojo> category;
    public ArrayList<EffectsPojo> effect_data;

    public ArrayList<EffectsPojo> getEffect_data() {
        return effect_data;
    }

    public void setEffect_data(ArrayList<EffectsPojo> effect_data) {
        this.effect_data = effect_data;
    }

    public ArrayList<CategoryPojo> getCategoryPojoArrayList() {
        return category;
    }

    public void setCategoryPojoArrayList(ArrayList<CategoryPojo> categoryPojoArrayList) {
        this.category = categoryPojoArrayList;
    }

    public MeditationPojo getMeditation() {
        return meditation;
    }

    public void setMeditation(MeditationPojo meditation) {
        this.meditation = meditation;
    }

    public TodaySpecialPojo getToday_special() {
        return today_special;
    }

    public void setToday_special(TodaySpecialPojo today_special) {
        this.today_special = today_special;
    }

    public ArrayList<MostPopularPojo> getMost_popular_songs() {
        return most_popular_songs;
    }

    public void setMost_popular_songs(ArrayList<MostPopularPojo> most_popular_songs) {
        this.most_popular_songs = most_popular_songs;
    }

    public ArrayList<songsPojo> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<songsPojo> songs) {
        this.songs = songs;
    }
}
