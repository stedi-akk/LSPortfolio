package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LsApp implements Serializable {
    private long id;
    private String name;
    @SerializedName("icon")
    private String iconUrl;
    private String description;
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

    public String getDescription() {
        return description;
    }

    public List<String> getGalleryUrls() {
        return galleryUrls;
    }
}
