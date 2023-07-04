package com.schoolmanagement.schoolmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {
    private String addressText;
    private String city;
    private Long pinCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("id")
    @JoinColumn(
            name = "district_id",
            referencedColumnName = "id"

    )
    private District district;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("id")
    @JoinColumn(
            name = "state_id",
            referencedColumnName = "id"

    )
    private State state;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("id")
    @JoinColumn(
            name = "country_id",
            referencedColumnName = "id"

    )
    private Country country;
}
