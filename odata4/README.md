# ODATA 4 with Apache Olingo

## Documentation 
Development of Olingo - https://github.com/subash-m/Spring-Boot-Olingo-OData-V4.01

With Springboot - https://github.com/subash-m/Spring-Boot-Olingo-OData-V4.01

## Testing 
1. get data models
 
http://localhost:8080/OData/V1.0/$metadata

2. test data
   
http://localhost:8080/OData/V1.0/teams?$format=JSON <br />
http://localhost:8080/OData/V1.0/teams(1) <br />
http://localhost:8080/OData/V1.0/teams(1)/firstName <br />
http://localhost:8080/OData/V1.0/teams?$format=JSON&$skip=2&$top=3&$count=true <br />
http://localhost:8080/OData/V1.0/teams?$format=JSON&$top=3&$select=email <br />
http://localhost:8080/OData/V1.0/teams?$format=JSON&$top=3&$orderby=firstName%20desc <br />