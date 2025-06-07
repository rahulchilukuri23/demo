-- Fuel eligibility
INSERT INTO ev_management.fuel_eligibility(description)
SELECT DISTINCT fuel_eligibility
FROM ev_management.staging_ev
WHERE fuel_eligibility IS NOT NULL
ON CONFLICT DO NOTHING;

-- Vehicle types
INSERT INTO ev_management.vehicle_type(type)
SELECT DISTINCT vehicle_type
FROM ev_management.staging_ev
WHERE vehicle_type IS NOT NULL
ON CONFLICT DO NOTHING;

-- Utilities (split by '||')
INSERT INTO ev_management.utility(name)
SELECT DISTINCT TRIM(unnest(string_to_array(utility, '||')))
FROM ev_management.staging_ev
WHERE utility IS NOT NULL
ON CONFLICT DO NOTHING;

-- Locations
INSERT INTO ev_management.location(county, city, state_code, postal_code, census_tract, coordinates)
SELECT DISTINCT
    county, city, state, postal_code, census_tract,
    ST_GeogFromText(REPLACE(vehicle_location, 'POINT', 'POINT'))
FROM ev_management.staging_ev
WHERE postal_code IS NOT NULL AND TRIM(postal_code) != ''
ON CONFLICT DO NOTHING;

-- Vehicle models
INSERT INTO ev_management.vehicle_model(make, model, model_year)
SELECT DISTINCT make, model, model_year
FROM ev_management.staging_ev
ON CONFLICT DO NOTHING;

-- Vehicles (without utility_id)
INSERT INTO ev_management.vehicle(
    vin, model_id, type_id, fuel_eligibility_id, electric_range, base_msrp,
    dol_vehicle_id, legislative_district, location_id
)
SELECT
    s.vin,
    vm.id,
    vt.id,
    fe.id,
    s.electric_range,
    s.base_msrp,
    s.dol_vehicle_id,
    s.legislative_district,
    l.id
FROM ev_management.staging_ev s
JOIN ev_management.vehicle_model vm ON s.make = vm.make AND s.model = vm.model AND s.model_year = vm.model_year
JOIN ev_management.vehicle_type vt ON s.vehicle_type = vt.type
JOIN ev_management.fuel_eligibility fe ON s.fuel_eligibility = fe.description
JOIN ev_management.location l ON
    s.city = l.city AND
    s.county = l.county AND
    s.state = l.state_code AND
    s.postal_code = l.postal_code AND
    s.census_tract = l.census_tract
ON CONFLICT (vin) DO NOTHING;

-- Vehicle ↔ Utilities many-to-many inserts
INSERT INTO ev_management.vehicle_utility(vehicle_id, utility_id)
SELECT DISTINCT
    v.id AS vehicle_id,
    u.id AS utility_id
FROM ev_management.staging_ev s
JOIN ev_management.vehicle v ON v.vin = s.vin
JOIN LATERAL unnest(string_to_array(s.utility, '||')) AS util_name(name)
JOIN ev_management.utility u ON u.name = TRIM(util_name.name)
WHERE s.utility IS NOT NULL
ON CONFLICT DO NOTHING;

DROP TABLE IF EXISTS ev_management.staging_ev;
