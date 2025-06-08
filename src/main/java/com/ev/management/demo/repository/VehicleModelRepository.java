package com.ev.management.demo.repository;

import com.ev.management.demo.entity.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface   VehicleModelRepository extends JpaRepository<VehicleModel, Long> {
    Optional<VehicleModel> findByMakeIgnoreCaseAndModelIgnoreCaseAndModelYear(String make, String model, Integer year);
    Optional<List<VehicleModel>> findByModelIgnoreCase(String model);
}
