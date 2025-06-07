package com.ev.management.demo.repository;

import com.ev.management.demo.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilityRepository extends JpaRepository<Utility, Long> {
    Optional<Utility> findByName(String name);
}
