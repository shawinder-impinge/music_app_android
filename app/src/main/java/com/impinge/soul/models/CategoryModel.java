package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {
    int id;
    String name, title;
    ArrayList<CategoryImages> images;
    String author_name, author_image, cover_images, status, description, listen_count;
    ArrayList<CategorySongs> songs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<CategoryImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<CategoryImages> images) {
        this.images = images;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListen_count() {
        return listen_count;
    }

    public void setListen_count(String listen_count) {
        this.listen_count = listen_count;
    }

    public ArrayList<CategorySongs> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<CategorySongs> songs) {
        this.songs = songs;
    }


    @Override
    public String toString() {
        return name;
    }
}
