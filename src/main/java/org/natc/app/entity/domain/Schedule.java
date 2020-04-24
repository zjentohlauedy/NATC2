package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDate;

@Entity(name = "schedule_t")
@IdClass(ScheduleId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    private String year;
    @Id
    private Integer sequence;
    private Integer type;
    private String data;
    private LocalDate scheduled;
    private Integer status;
}