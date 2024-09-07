package org.natc.app.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "schedule_t")
@IdClass(ScheduleId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    public static final Integer FIRST_SEQUENCE = 1;

    @Id
    private String year;
    @Id
    private Integer sequence;
    private Integer type;
    private String data;
    private LocalDate scheduled;
    private Integer status;
}
