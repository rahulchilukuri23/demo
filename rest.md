* REST Endpoints
* POST /ev-management/vehicle/add(assuming method is inferred)
```
curl -k -H "Content-Type: application/json" http://localhost:8080/ev-management/vehicle/add 
-d '{"vin":"5YJ3E1EB6K","county":"King","city":"Seattle","state":"WA","postal_code":"98178","model_year":2019,"make":"TESLA","model":"MODEL 3","vehicle_type":"Battery Electric Vehicle (BEV)",
     "fuel_eligibility":"Clean Alternative Fuel Vehicle Eligible","electric_range":220,"base_msrp":0,"legislative_district":37,"dol_vehicle_id":101250425,
     "vehicle_location":"POINT (-122.23825 47.49461)","utility":"CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)","census_tract":53033011902}'
     
response:
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 15302
{
  "vin": "5YJ3E1EB6K",
  "county": "King",
  "city": "Seattle",
  "state": "WA",
  "postal_code": "98178",
  "model_year": 2019,
  "make": "TESLA",
  "model": "MODEL 3",
  "vehicle_type": "Battery Electric Vehicle (BEV)",
  "fuel_eligibility": "Clean Alternative Fuel Vehicle Eligible",
  "electric_range": 220,
  "base_msrp": 0,
  "legislative_district": 37,
  "dol_vehicle_id": 101250425,
  "vehicle_location": "POINT (-122.23825 47.49461)",
  "utility": "CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)",
  "census_tract": 53033011902
}
     
 ```
* GET /ev-management/vehicle/all
```
  curl -k -H "Accept: application/json" http://localhost:8080/ev-management/vehicle/all
  
response:
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 31256
[
  {
    "vin": "5YJ3E1EB6K",
    "county": "King",
    "city": "Seattle",
    "state": "WA",
    "postal_code": "98178",
    "model_year": 2019,
    "make": "TESLA",
    "model": "MODEL 3",
    "vehicle_type": "Battery Electric Vehicle (BEV)",
    "fuel_eligibility": "Clean Alternative Fuel Vehicle Eligible",
    "electric_range": 220,
    "base_msrp": 0,
    "legislative_district": 37,
    "dol_vehicle_id": 101250425,
    "vehicle_location": "POINT (-122.23825 47.49461)",
    "utility": "CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)",
    "census_tract": 53033011902
  },
  {
    "vin": "5YJ3E1EB6L",
    "county": "Pierce",
    "city": "Tacoma",
    "state": "WA",
    "postal_code": "98402",
    "model_year": 2020,
    "make": "TESLA",
    "model": "MODEL Y",
    "vehicle_type": "Battery Electric Vehicle (BEV)",
    "fuel_eligibility": "Clean Alternative Fuel Vehicle Eligible",
    "electric_range": 330,
    "base_msrp": 48199.99,
    "legislative_district": 37,
    "dol_vehicle_id": 101250426,
    "vehicle_location": "POINT (-122.43725 47.24361)",
    "utility": "CITY OF TACOMA - (WA)",
    "census_tract": 53033011903
  }
]
  
```
* GET /ev-management/vehicle/{vin}
```
curl -k -H "Accept: application/json" http://localhost:8080/ev-management/vehicle/5YJ3E1EB6K

response:
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 15302
{
  "vin": "5YJ3E1EB6K",
  "county": "King",
  "city": "Seattle",
  "state": "WA",
  "postal_code": "98178",
  "model_year": 2019,
  "make": "TESLA",
  "model": "MODEL 3",
  "vehicle_type": "Battery Electric Vehicle (BEV)",
  "fuel_eligibility": "Clean Alternative Fuel Vehicle Eligible",
  "electric_range": 220,
  "base_msrp": 0,
  "legislative_district": 37,
  "dol_vehicle_id": 101250425,
  "vehicle_location": "POINT (-122.23825 47.49461)",
  "utility": "CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)",
  "census_tract": 53033011902
}

``` 
* PUT /ev-management/vehicle/update/{vin}
```
curl -k -X PUT -H "Content-Type: application/json" http://localhost:8080/ev-management/vehicle/5YJ3E1EB6K 
-d '{"vin":"5YJ3E1EB6K","county":"King","city":"Seattle","state":"WA","postal_code":"98178","model_year":2019,"make":"TESLA","model":"MODEL 3","vehicle_type":"Battery Electric Vehicle (BEV)",
     "fuel_eligibility":"Clean Alternative Fuel Vehicle Eligible","electric_range":240,"base_msrp":0,"legislative_district":37,"dol_vehicle_id":101250425,
     "vehicle_location":"POINT (-122.23825 47.49461)","utility":"CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)","census_tract":53033011902}'

response:
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 15302
{
  "vin": "5YJ3E1EB6K",
  "county": "King",
  "city": "Seattle",
  "state": "WA",
  "postal_code": "98178",
  "model_year": 2019,
  "make": "TESLA",
  "model": "MODEL 3",
  "vehicle_type": "Battery Electric Vehicle (BEV)",
  "fuel_eligibility": "Clean Alternative Fuel Vehicle Eligible",
  "electric_range": 220,
  "base_msrp": 0,
  "legislative_district": 37,
  "dol_vehicle_id": 101250425,
  "vehicle_location": "POINT (-122.23825 47.49461)",
  "utility": "CITY OF SEATTLE - (WA)|CITY OF TACOMA - (WA)",
  "census_tract": 53033011902
}
     
```
* PUT /ev-management/vehicle/update/msrp/{model}
```
curl -k -H "Content-Type: application/json" http://localhost:8080/ev-management/vehicle/update/msrp/MODEL%20Y -d '{"model":"MODEL Y","base_msrp":48199.99}'

Response:
HTTP/1.1 200 OK
Content-Type: text/plain
Content-Length: 300
Vehicle MSRP updated for all vehicles matching model MODEL Y
``` 
* DELETE /ev-management/vehicle/{vin}
```
curl -k -X DELETE http://localhost:8080/ev-management/vehicle/5YJ3E1EB6K

Response:
HTTP/1.1 200 OK
Content-Type: text/plain
Content-Length: 153
Vehicle Deleted
``` 