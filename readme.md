# Documentation

# Local setup requirements
* Java 21 or higher
* Docker
* IDE to run spring boot application

# DB Schema Setup
* sql scripts are located at /src/main/resources/db
* Ideally these scripts are to be run on postgresql server along with the csv file from https://data.wa.gov/api/views/f6w7-q2d2/rows.csv?accessType=DOWNLOAD
* Need to update the path of the file on load-schema-staging-table.sql first when that script is to be run
* Run these commands to prepopulate data from the csv file
```
psql -U ev_management_user -d ev_management -f schema.sql
psql -U ev_management_user -d ev_management -f load-schema-staging-table.sql
psql -U ev_management_user -d ev_management -f load-schema.sql
```
* But running docker compose up at root of project will provision docker containers for database to be used locally
* Open a sql client and connect to this db with the credentials laid out in docker compose file
* run the schema.sql, load ev_staging table manually(gui) and point it to csv file, load-schema.sql from under src/main/resources/db directory
 
# Application
* Run the spring boot application in an IDE of your choice
* Please follow the below documentation on rest endpoints
  * [REST ENDPOINTS](rest.md)
* Alternatively spring rest docs can be utilized her(ignore the rest of the auto generated rest end points)
  * http://localhost:8080/swagger-ui/index.html#/vehicle-controller
 
# Observability
* Prometheus scraping metrics and grafana for monitoring
  under root, monitoring directory contains the necessary configuration for observability
* Workflow
 
  ![img.png](img.png)
* Prometheus UI:
  ``` 
  http://localhost:9090/status
  http://localhost:9090/config
  http://localhost:9090/targets
  http://localhost:9090/service-discovery?search=#EvManagementMetrics
  Prometheus metrics end point: http://localhost:8080/actuator/prometheus
  ```
* Metrics can be obtained using the url. 
  Default credentials admin/admin, skip credential change on prompt
  ``` 
  http://localhost:3000/
  ```
* Below metrics can be added in graphana dashboard as visualization
```
spring_data_repository_invocations_seconds_count
spring_data_repository_invocations_seconds_sum
spring_data_repository_invocations_seconds_max
http_server_requests_seconds_bucket
```
* A dashboard is provisioned [here](http://localhost:3000/d/ev-management-metrics/application-metrics-dashboard?orgId=1&refresh=15s)

# Deploying with Helm Charts
* Application can be deployed with helm chart using the command
* Helm chart components include templates for deployment, service, hpa. 
  No config maps and secrets as the config is now using application.properties
```
helm upgrade --install ev-management ./helmchart 
```

# Limitations && TODO
  * Unit tests
  * POST/PUT location conversion from text to point and vice versa
  * VIN data is not unique in spreadsheet.
    Vehicle to Utility mapping in load schema needs to be fixed because of this issue
  * Maintaining container registry and using suggested docker registry
  * helm chart deployment
