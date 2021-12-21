package com.impinge.soul.models;

import java.util.ArrayList;

public class EffectsPojo {

    public int id;
    public int listen_count;
    public String name;
    public String title;
    public String author_name;
    public String author_image;
    public String cover_images;
    public String description;
    public boolean status;
    public ArrayList<EffectsAlbumPojo> effect_album;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListen_count() {
        return listen_count;
    }

    public void setListen_count(int listen_count) {
        this.listen_count = listen_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getCover_images() {
        return cover_images;
    }

    public void setCover_images(String cover_images) {
        this.cover_images = cover_images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<EffectsAlbumPojo> getEffect_album() {
        return effect_album;
    }

    public void setEffect_album(ArrayList<EffectsAlbumPojo> effect_album) {
        this.effect_album = effect_album;
    }
}
