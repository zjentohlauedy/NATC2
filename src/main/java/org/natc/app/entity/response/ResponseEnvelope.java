package org.natc.app.entity.response;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

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

    public ResponseEnvelope(final List<T> resources) {
        this.resources = resources;

        status = CollectionUtils.isEmpty(resources) ? ResponseStatus.NOT_FOUND : ResponseStatus.SUCCESS;
    }
}
