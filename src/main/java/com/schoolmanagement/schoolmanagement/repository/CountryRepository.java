package com.schoolmanagement.schoolmanagement.repository;

import com.schoolmanagement.schoolmanagement.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findByName(String countryName);
}
