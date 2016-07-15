package com.stedi.lsportfolio.model;

import java.util.List;

public final class LsAllApps {
    private static LsAllApps instance;

    private List<LsApp> apps;

    private LsAllApps() {
    }

    public static LsAllApps getInstance() {
        if (instance == null)
            instance = new LsAllApps();
        return instance;
    }

    public void setApps(List<LsApp> apps) {
        this.apps = apps;
    }

    public List<LsApp> getApps() {
        return apps;
    }
}
