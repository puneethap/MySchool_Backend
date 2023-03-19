package com.schoolmanagement.schoolmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "states", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "country_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne()
    @JoinColumn(
            name = "country_id",
            referencedColumnName = "id"

    )
    private Country country;

    public State(String name, Country country) {
        this.name = name;
        this.country = country;
    }
}
