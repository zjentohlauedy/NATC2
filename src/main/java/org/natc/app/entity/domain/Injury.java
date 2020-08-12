package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "injuries_t")
@IdClass(InjuryId.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Injury {
    @Id
    private Integer gameId;
    @Id
    private Integer playerId;
    private Integer teamId;
    private Integer duration;
}
