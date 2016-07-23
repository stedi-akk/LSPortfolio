package com.stedi.lsportfolio.other;

import java.io.IOException;

public class ServerException extends IOException {
    public ServerException(String detailMessage) {
        super(detailMessage);
    }
}
