package com.stedi.lsportfolio.api;

import rx.Observable;

public interface Api {
    Observable<ResponseLsAllApps> requestLsAllApps();

    Observable<ResponseLsApp> requestLsApp(long id);
}
