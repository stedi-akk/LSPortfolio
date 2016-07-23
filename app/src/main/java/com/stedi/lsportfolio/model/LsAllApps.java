package com.stedi.lsportfolio.model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class LsAllApps {
    private List<LsApp> apps;

    @Inject
    public LsAllApps() {
    }

    public void setApps(List<LsApp> apps) {
        this.apps = apps;
    }

    public List<LsApp> getApps() {
        return apps;
    }
}
