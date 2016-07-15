package com.stedi.lsportfolio.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LsApp implements Serializable {
    private long id;
    private String name;
    @SerializedName("icon")
    private String iconUrl;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
