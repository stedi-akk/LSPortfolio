package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LsAppDetailed extends LsApp implements Serializable {
    private String description;
    @SerializedName("gallery")
    private List<String> galleryUrls;
    @SerializedName("link")
    private List<StoreLink> storeLinks;

    public String getDescription() {
        return description;
    }

    public List<String> getGalleryUrls() {
        return galleryUrls;
    }

    public List<StoreLink> getStoreLinks() {
        return storeLinks;
    }
}
