package org.natc.app.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
