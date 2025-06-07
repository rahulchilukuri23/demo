-- 1. Create the schema
CREATE SCHEMA IF NOT EXISTS ev_management;

-- 2. Create the user with a secure password
CREATE ROLE ev_management_user WITH LOGIN PASSWORD 'A35Hj5e%^Z';

-- 3. Grant privileges on the schema
GRANT USAGE, CREATE ON SCHEMA ev_management TO ev_management_user;

-- 4. Grant table-level privileges (for all existing tables — run again if new ones are added)
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON ALL TABLES IN SCHEMA ev_management TO ev_management_user;

-- 5. Fuel eligibility type
CREATE TABLE IF NOT EXISTS ev_management.fuel_eligibility (
    id SERIAL PRIMARY KEY,
    description TEXT UNIQUE NOT NULL
);

-- 6. Vehicle type (e.g., BEV, PHEV)
CREATE TABLE IF NOT EXISTS ev_management.vehicle_type (
    id SERIAL PRIMARY KEY,
    type TEXT UNIQUE NOT NULL
);

-- 7.  Geographic location
CREATE EXTENSION IF NOT EXISTS postgis;  -- for GEOGRAPHY type

-- 8. Vehicle Location
CREATE TABLE IF NOT EXISTS ev_management.location (
    id SERIAL PRIMARY KEY,
    county TEXT,
    city TEXT,
    state_code CHAR(2),
    postal_code CHAR(5),
    census_tract TEXT,
    coordinates GEOGRAPHY(POINT, 4326)
);

-- 9. Vehicle model info
CREATE TABLE IF NOT EXISTS ev_management.vehicle_model (
    id SERIAL PRIMARY KEY,
    make TEXT NOT NULL,
    model TEXT NOT NULL,
    model_year INTEGER NOT NULL,
    UNIQUE(make, model, model_year)
);

-- 10. Vehicles
CREATE TABLE IF NOT EXISTS ev_management.vehicles (
    id SERIAL PRIMARY KEY,
    vin TEXT UNIQUE NOT NULL,
    model_id INT REFERENCES ev_management.vehicle_model(id),
    type_id INT REFERENCES ev_management.vehicle_type(id),
    fuel_eligibility_id INT REFERENCES ev_management.fuel_eligibility(id),
    electric_range INT,
    base_msrp NUMERIC,
    dol_vehicle_id BIGINT,
    legislative_district INT,
    location_id INT REFERENCES ev_management.location(id),
    utility_id INT REFERENCES ev_management.utilities(id)
);

-- 11. Bridge table: vehicle ↔ utilities (for multiple utility providers)
CREATE TABLE IF NOT EXISTS ev_management.vehicle_utilities (
    vehicle_id INT REFERENCES ev_management.vehicles(id),
    utility_id INT REFERENCES ev_management.utilities(id),
    PRIMARY KEY (vehicle_id, utility_id)
);

-- 12. Staging table (flat CSV)
CREATE TABLE IF NOT EXISTS ev_management.staging_ev (
    vin TEXT,
    county TEXT,
    city TEXT,
    state CHAR(2),
    postal_code CHAR(5),
    model_year INT,
    make TEXT,
    model TEXT,
    vehicle_type TEXT,
    fuel_eligibility TEXT,
    electric_range INT,
    base_msrp NUMERIC,
    legislative_district TEXT,
    dol_vehicle_id BIGINT,
    vehicle_location TEXT, -- You may want to convert this to GEOGRAPHY(POINT, 4326)
    utility TEXT,
    census_tract TEXT
);
