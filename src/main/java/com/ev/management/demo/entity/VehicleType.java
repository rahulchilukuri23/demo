package com.ev.management.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "vehicle_type",
        schema = "ev_management",
        uniqueConstraints = @UniqueConstraint(columnNames = "type")
)
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, unique = true, length = 50)
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
