package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.model.LsApp;

import java.util.List;

public class ResponseLsAllApps {
    private Data data;

    private class Data {
        List<LsApp> portfolio;
    }

    public List<LsApp> getApps() {
        return data.portfolio;
    }
}
