package org.natc.natc.entity.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseEnvelope<T> {
    private ResponseStatus status;
    private T resource;
    private List<T> resources;
    private List<String> errors;

    public ResponseEnvelope(final ResponseStatus responseStatus, List<T> list) {
        status = responseStatus;
        resources = list;
    }
}
