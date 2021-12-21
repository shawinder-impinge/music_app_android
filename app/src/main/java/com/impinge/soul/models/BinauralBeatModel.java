package com.impinge.soul.models;

import java.io.Serializable;

public class BinauralBeatModel implements Serializable {

    public int beatId;
    private String beatName;
    private String beatCover;

    public BinauralBeatModel(int beatId, String beatName, String beatCover) {
        this.beatId = beatId;
        this.beatName = beatName;
        this.beatCover = beatCover;
    }

    public int getBeatId() {
        return beatId;
    }

    public void setBeatId(int beatId) {
        this.beatId = beatId;
    }

    public String getBeatName() {
        return beatName;
    }

    public void setBeatName(String beatName) {
        this.beatName = beatName;
    }

    public String getBeatCover() {
        return beatCover;
    }

    public void setBeatCover(String beatCover) {
        this.beatCover = beatCover;
    }
}
