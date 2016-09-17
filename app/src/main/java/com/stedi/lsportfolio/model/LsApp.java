package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LsApp implements Serializable {
    private long id;
    private String name;
    @SerializedName("icon")
    private String iconUrl;
    private String description;

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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
