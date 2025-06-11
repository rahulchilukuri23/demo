#!/bin/bash
# Variables
CONTAINER_NAME=postgresql
DB_USER=ev_management_user
DB_NAME=ev_management
CSV_PATH="/docker-entrypoint-initdb.d/data.csv"
STAGING_TABLE="ev_management.staging_ev"

# Load CSV into staging table
echo "Loading CSV data...into $STAGING_TABLE"
psql -U "$DB_USER" -d "$DB_NAME" -c "\copy $STAGING_TABLE FROM '$CSV_PATH' CSV HEADER;"

echo "Load complete."