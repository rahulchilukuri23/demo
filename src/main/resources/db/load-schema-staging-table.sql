--Server
COPY staging_ev FROM '/absolute/path/to/your_ev_file.csv' DELIMITER ',' CSV HEADER;

--Client
\copy ev_management.staging_ev FROM 'C:\Users\indir\Downloads\Electric_Vehicle_Population_Data.csv'  WITH CSV HEADER;