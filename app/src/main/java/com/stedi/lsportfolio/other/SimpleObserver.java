package com.stedi.lsportfolio.other;

import rx.Observer;

public class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T t) {
    }
}
