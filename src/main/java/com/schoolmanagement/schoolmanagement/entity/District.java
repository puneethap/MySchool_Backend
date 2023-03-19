package com.schoolmanagement.schoolmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "districts", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "state_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(
            name = "state_id",
            referencedColumnName = "id"
    )
    private State state;

    public District(String name, State state) {
        this.name = name;
        this.state = state;
    }
}
