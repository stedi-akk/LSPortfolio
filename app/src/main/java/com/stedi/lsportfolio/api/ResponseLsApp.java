package com.stedi.lsportfolio.api;

import com.google.gson.annotations.SerializedName;
import com.stedi.lsportfolio.model.LsApp;

public class ResponseLsApp {
    @SerializedName("data")
    private LsApp app;

    public LsApp getApp() {
        return app;
    }
}
