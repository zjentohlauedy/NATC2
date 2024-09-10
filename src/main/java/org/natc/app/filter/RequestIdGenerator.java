package org.natc.app.filter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestIdGenerator {
    public static final String REQUEST_ID_KEY = "request-id";

    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
