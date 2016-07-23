package com.stedi.lsportfolio.api;

import com.google.gson.annotations.SerializedName;
import com.stedi.lsportfolio.model.LsAppDetailed;

public class ResponseLsApp extends BaseResponse {
    @SerializedName("data")
    private LsAppDetailed app;

    public LsAppDetailed getApp() {
        return app;
    }
}
