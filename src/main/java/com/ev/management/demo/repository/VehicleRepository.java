package com.ev.management.demo.repository;

import com.ev.management.demo.entity.Vehicle;
import com.ev.management.demo.entity.VehicleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    @EntityGraph(attributePaths = {
            "model",
            "type",
            "fuelEligibility",
            "location",
            "utility"
    })
    Optional<Vehicle> findByVin(String vin);

    boolean existsByVin(String vin);
    void deleteByVin(String vin);
    List<Vehicle> findByModelId(Long modelId);
}
