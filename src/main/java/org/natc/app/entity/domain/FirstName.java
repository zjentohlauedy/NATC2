package org.natc.app.entity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
