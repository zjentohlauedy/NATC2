package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "firstnames_t")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirstName {

    @Id
    private String name;
    private Double frequency;
}
