package com.schoolmanagement.schoolmanagement.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "countries", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Country(String name) {
        this.name = name;
    }
}
