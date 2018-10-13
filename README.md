# rateLimit

Assumptions:
1. Default Rate Allowed : 5 requests / api key
2. Default suspend time :  1 min.

How to run the application?
mvn clean package
java -jar target/ratelimit-1.0-SNAPSHOT.jar

Mandatory Props:
1. City name 
2. api key

Sample requests:
curl -X GET \
  'http://localhost:8080/hotels/Bangkok?order=desc' \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: 20b466e8-bf93-468f-80b0-b504e4930370' \
  -H 'api_key: 1234'
  
curl -X GET \
  'http://localhost:8080/hotels/Ashburn?' \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: 20b466e8-bf93-468f-80b0-b504e4930370' \
  -H 'api_key: 1234'  
  
Future Enhancements
1. Validate the API key. 
2. Support rate limit at different API level. 
  
