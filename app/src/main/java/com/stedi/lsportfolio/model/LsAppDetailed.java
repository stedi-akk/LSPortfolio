package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LsAppDetailed extends LsApp implements Serializable {
    @SerializedName("text")
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGalleryUrls(List<String> galleryUrls) {
        this.galleryUrls = galleryUrls;
    }

    public void setStoreLinks(List<StoreLink> storeLinks) {
        this.storeLinks = storeLinks;
    }
}
