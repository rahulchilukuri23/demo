package com.ev.management.demo.repository;

import com.ev.management.demo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByPostalCode(String postalCode);
}
