package com.stedi.lsportfolio.model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class Model {
    private List<LsApp> apps;
    private List<LsJob> jobs;

    @Inject
    public Model() {
    }

    public void setApps(List<LsApp> apps) {
        this.apps = apps;
    }

    public List<LsApp> getApps() {
        return apps;
    }

    public void setJobs(List<LsJob> jobs) {
        this.jobs = jobs;
    }

    public List<LsJob> getJobs() {
        return jobs;
    }
}
