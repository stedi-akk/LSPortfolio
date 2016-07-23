package com.stedi.lsportfolio.api;

import com.stedi.lsportfolio.other.ServerException;

public abstract class BaseResponse {
    private int status;

    public <T> T validate() throws ServerException {
        if (status != 200)
            throw new ServerException("status != 200");
        return (T) this;
    }
}
