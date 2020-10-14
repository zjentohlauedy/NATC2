package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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
