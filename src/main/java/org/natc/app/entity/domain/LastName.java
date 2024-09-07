package org.natc.app.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "lastnames_t")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LastName {

    @Id
    private String name;
    private Double frequency;
}
