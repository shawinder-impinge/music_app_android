package com.impinge.soul.models;

import java.io.Serializable;

public class SearchSongModel implements Serializable {
    String id, name, songs_length, songs_length_in_sec, is_user_fav, title, audios, cover_images, description, listen_count, status, author_name, author_image;

    public String getId() {
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

    public String getSongs_length() {
        return songs_length;
    }

    public void setSongs_length(String songs_length) {
        this.songs_length = songs_length;
    }

    public String getSongs_length_in_sec() {
        return songs_length_in_sec;
    }

    public void setSongs_length_in_sec(String songs_length_in_sec) {
        this.songs_length_in_sec = songs_length_in_sec;
    }

    public String getIs_user_fav() {
        return is_user_fav;
    }

    public void setIs_user_fav(String is_user_fav) {
        this.is_user_fav = is_user_fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudios() {
        return audios;
    }

    public void setAudios(String audios) {
        this.audios = audios;
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

    public String getListen_count() {
        return listen_count;
    }

    public void setListen_count(String listen_count) {
        this.listen_count = listen_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
