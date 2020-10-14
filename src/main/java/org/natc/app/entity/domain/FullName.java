package org.natc.app.entity.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FullName {

    private String firstName;
    private String lastName;
}
