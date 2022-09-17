package com.schoolmanagement.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Erole name;

    public Role() {
    }

    public Role(Integer id, Erole name) {
        this.id = id;
        this.name = name;
    }
}
