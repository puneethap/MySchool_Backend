package com.schoolmanagement.schoolmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdmissionEnquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String admissionSeekingStandard;
    private String parentName;
    private Long phoneNumber;
    private String emailId;
    private LocalDate enquiredDate;

    @Embedded
    private Address address;

}
