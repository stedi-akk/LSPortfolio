package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.model.LsApp;
import com.stedi.lsportfolio.model.LsJob;

import java.util.List;

public class ResponseLsAllApps extends BaseResponse {
    private Data data;

    private class Data {
        List<LsApp> portfolio;
        List<LsJob> work;
    }

    public List<LsApp> getApps() {
        return data.portfolio;
    }

    public List<LsJob> getJobs() {
        return data.work;
    }
}
