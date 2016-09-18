package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

public class LsJob {
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("file")
    private String infoUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
}
