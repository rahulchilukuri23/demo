-- 1. Create the schema
CREATE SCHEMA IF NOT EXISTS ev_management;

-- 2. Create the user with a secure password
CREATE ROLE IF NOT EXISTS ev_management_user WITH LOGIN PASSWORD 'A35Hj5e%^Z';

-- 3. Grant privileges on the schema
GRANT USAGE, CREATE ON SCHEMA ev_management TO ev_management_user;

-- 4. Grant table-level privileges (re-run if new tables added)
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON ALL TABLES IN SCHEMA ev_management TO ev_management_user;

-- 5. Enable PostGIS for spatial data support
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS postgis_raster;

-- 6. Fuel eligibility type (CAFV status)
CREATE TABLE IF NOT EXISTS ev_management.fuel_eligibility (
          id BIGSERIAL PRIMARY KEY,
          description VARCHAR(255) UNIQUE NOT NULL
);

-- 7. Vehicle type (e.g., BEV, PHEV)
CREATE TABLE IF NOT EXISTS ev_management.vehicle_type (
      id BIGSERIAL PRIMARY KEY,
      type VARCHAR(50) UNIQUE NOT NULL
);

-- 8. Electric utilities
CREATE TABLE IF NOT EXISTS ev_management.utility (
 id BIGSERIAL PRIMARY KEY,
 name VARCHAR(255) UNIQUE NOT NULL
);

-- 9. Geographic location
CREATE TABLE IF NOT EXISTS ev_management.location (
  id BIGSERIAL PRIMARY KEY,
  county VARCHAR(50),
  city VARCHAR(50),
  state_code varchar(2),
  postal_code VARCHAR(10),
  census_tract BIGINT,
  coordinates GEOGRAPHY(POINT, 4326)
);

-- 10. Vehicle model info
CREATE TABLE IF NOT EXISTS ev_management.vehicle_model (
       id BIGSERIAL PRIMARY KEY,
       make VARCHAR(50) NOT NULL,
       model VARCHAR(50) NOT NULL,
       model_year INT NOT NULL,
       UNIQUE(make, model, model_year)
);

-- 11. Vehicle records (no utility_id here now)
CREATE TABLE IF NOT EXISTS ev_management.vehicle (
 id BIGSERIAL PRIMARY KEY,
 vin VARCHAR(50) UNIQUE NOT NULL,
 model_id BIGINT REFERENCES ev_management.vehicle_model(id),
 type_id BIGINT REFERENCES ev_management.vehicle_type(id),
 fuel_eligibility_id BIGINT REFERENCES ev_management.fuel_eligibility(id),
 electric_range INT,
 base_msrp NUMERIC,
 dol_vehicle_id BIGINT,
 legislative_district INT,
 location_id BIGINT REFERENCES ev_management.location(id)
);
CREATE INDEX IF NOT EXISTS idx_vin ON ev_management.vehicle(vin);
CREATE INDEX IF NOT EXISTS idx_vehicle_model_id ON ev_management.vehicle(model_id);

-- 12. Vehicle ↔ Utility many-to-many join table
CREATE TABLE IF NOT EXISTS ev_management.vehicle_utility (
         vehicle_id BIGINT NOT NULL REFERENCES ev_management.vehicle(id) ON DELETE CASCADE,
         utility_id BIGINT NOT NULL REFERENCES ev_management.utility(id) ON DELETE CASCADE,
         PRIMARY KEY (vehicle_id, utility_id)
);

-- 13. Staging table (for raw CSV imports)
CREATE TABLE IF NOT EXISTS ev_management.staging_ev (
    vin VARCHAR(50),
    county VARCHAR(50),
    city VARCHAR(50),
    state CHAR(2),
    postal_code VARCHAR(10),
    model_year INT,
    make VARCHAR(50),
    model VARCHAR(50),
    vehicle_type VARCHAR(50),
    fuel_eligibility VARCHAR(255),
    electric_range INT,
    base_msrp NUMERIC,
    legislative_district INT,
    dol_vehicle_id BIGINT,
    vehicle_location TEXT,
    utility VARCHAR(255),
    census_tract BIGINT
);