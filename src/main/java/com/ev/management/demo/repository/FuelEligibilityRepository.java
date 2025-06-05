package com.ev.management.demo.repository;

import com.ev.management.demo.entity.FuelEligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuelEligibilityRepository extends JpaRepository<FuelEligibility, Long> {
    Optional<FuelEligibility> findByDescription(String description);
}
