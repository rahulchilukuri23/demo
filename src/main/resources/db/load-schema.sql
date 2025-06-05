-- Fuel eligibility
INSERT INTO fuel_eligibility(description)
SELECT DISTINCT fuel_eligibility
FROM staging_ev
WHERE fuel_eligibility IS NOT NULL
ON CONFLICT DO NOTHING;

-- Vehicle types
INSERT INTO vehicle_type(type)
SELECT DISTINCT vehicle_type
FROM staging_ev
WHERE vehicle_type IS NOT NULL
ON CONFLICT DO NOTHING;

-- Utilities (split by '||')
INSERT INTO utilities(name)
SELECT DISTINCT unnest(string_to_array(utility, '||'))
FROM staging_ev
WHERE utility IS NOT NULL
ON CONFLICT DO NOTHING;

-- Locations
INSERT INTO location(county, city, state, postal_code, census_tract, coordinates)
SELECT DISTINCT
    county, city, state, postal_code, census_tract,
    ST_GeogFromText(REPLACE(vehicle_location, 'POINT', 'POINT'))
FROM staging_ev
ON CONFLICT DO NOTHING;

-- Vehicle models
INSERT INTO vehicle_model(make, model, model_year)
SELECT DISTINCT make, model, model_year
FROM staging_ev
ON CONFLICT DO NOTHING;

-- Vehicles
INSERT INTO vehicles(vin, model_id, type_id, fuel_eligibility_id, electric_range, base_msrp, dol_vehicle_id, legislative_district, location_id)
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
FROM staging_ev s
JOIN vehicle_model vm ON s.make = vm.make AND s.model = vm.model AND s.model_year = vm.model_year
JOIN vehicle_type vt ON s.vehicle_type = vt.type
JOIN fuel_eligibility fe ON s.fuel_eligibility = fe.description
JOIN location l ON
    s.city = l.city AND
    s.county = l.county AND
    s.state = l.state AND
    s.postal_code = l.postal_code AND
    s.census_tract = l.census_tract
ON CONFLICT (vin) DO NOTHING;

-- Vehicle ↔ Utilities
INSERT INTO vehicle_utilities(vehicle_id, utility_id)
SELECT DISTINCT
    v.id,
    u.id
FROM staging_ev s
JOIN vehicles v ON v.vin = s.vin
JOIN unnest(string_to_array(s.utility, '||')) AS util(name)
JOIN utilities u ON u.name = util.name
ON CONFLICT DO NOTHING;
