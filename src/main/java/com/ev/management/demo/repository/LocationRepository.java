package com.ev.management.demo.repository;

import com.ev.management.demo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByPostalCode(String postalCode);
    Optional<Location> findByPostalCodeAndCityAndCountyAndStateCode(String postalCode, String city, String county, String state);
}
