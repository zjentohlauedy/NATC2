package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class TeamId implements Serializable {
    private Integer teamId;
    private String year;
}
