package com.ev.management.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "ev_management", name = "vehicle_model",
        uniqueConstraints = @UniqueConstraint(columnNames = {"make", "model", "model_year"}))
public class VehicleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;

    @Column(name = "model_year")
    private int modelYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }
}
