package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    private String year;
    private Integer sequence;
    private Integer type;
    private String data;
    private LocalDate scheduled;
    private Integer status;
}
