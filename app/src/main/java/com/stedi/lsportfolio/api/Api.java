package com.stedi.lsportfolio.api;

public interface Api {
    ResponseLsAllApps requestLsAllApps() throws Exception;

    ResponseLsApp requestLsApp(long id) throws Exception;
}
