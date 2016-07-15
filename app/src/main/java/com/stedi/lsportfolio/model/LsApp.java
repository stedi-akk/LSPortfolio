package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LsApp {
    private long id;
    private String name;
    @SerializedName("icon")
    private String iconUrl;
    @SerializedName("gallery")
    private List<String> galleryUrls;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public List<String> getGalleryUrls() {
        return galleryUrls;
    }
}
