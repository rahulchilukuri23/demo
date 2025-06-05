# Documentation

# DB Schema Setup
* sql scripts are located at /src/resources/db
* These scripts are to be scp'ed to the postgresql server along with the csv file from https://data.wa.gov/api/views/f6w7-q2d2/rows.csv?accessType=DOWNLOAD
* Run these commands to prepopulate data from the csv file
```
psql -U ev_management_user -d ev_management -f schema.sql
psql -U ev_management_user -d ev_management -f load-schema-staging-table.sql
psql -U ev_management_user -d ev_management -f load-schema.sql
```


# Deploying with Helm Charts