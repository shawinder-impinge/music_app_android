package com.musicapp.models;

import java.io.Serializable;

public class LoginVideoModel implements Serializable {

    VideoModel video;

    public VideoModel getVideo() {
        return video;
    }

    public void setVideo(VideoModel video) {
        this.video = video;
    }
}
