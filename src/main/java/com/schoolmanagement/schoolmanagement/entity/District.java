package com.schoolmanagement.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "districts", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "state_id"})})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "state_id",
            referencedColumnName = "id"
    )
    @JsonIncludeProperties("id")
    private State state;

    public District(String name, State state) {
        this.name = name;
        this.state = state;
    }
}
