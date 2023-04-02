package com.schoolmanagement.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "states", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "country_id"})})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "country_id",
            referencedColumnName = "id"

    )
    @JsonIncludeProperties("id")
    private Country country;

    public State(String name, Country country) {
        this.name = name;
        this.country = country;
    }
}
