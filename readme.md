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

# Application
* Postgresql server 
* Please follow the below documentation on rest endpoints
  * [REST ENDPOINTS](rest.md)
 
# Observability
* Prometheus scraping metrics and grafana for monitoring
  under root, observability directory contains the necessary configuration for observability
```
## at root to start prometheus and grafana
docker-compose up 
```
*  metrics can be obtained using the url
``` 
http://prometheus:9090
```


# Deploying with Helm Charts
* Application can be deployed with helm chart using the command
* Deploying in default namespace
```
helm upgrade --install ev-management ./helmchart 
```