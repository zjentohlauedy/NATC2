package org.natc.natc.entity.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseEnvelope<T> {
    private T resource;
    private List<T> resources;
    private List<String> errors;

    public ResponseEnvelope(List<T> list) {
        resources = list;
    }
}
