package org.natc.natc.entity.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ManagerId implements Serializable {
    private Integer managerId;
    private String year;
}
