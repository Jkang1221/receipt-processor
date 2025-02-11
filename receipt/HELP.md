**Prerequisites**
- Java 17
- Maven
- Docker & Docker Compose

**Build & Run with Docker** 

1. Build the Project:
```
mvn clean package
```

2. Build Docker Image:

```docker build -t receipt-processor.```

3. Run the Application:

```docker run -p 8080:8080 receipt-processor```
4. Alternatively, using Docker Compose:

```docker-compose up --build```

**API Documentation**

After starting the Docker container, access the Swagger UI:
```http://localhost:8080/swagger-ui/index.html```


**Process Receipt**

URL: /receipts/process

Method: POST

Content-Type: application/json

```
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}
```
Response:
```
Response body
{
"id": "b5065c40-fe0a-4193-baa9-531e572438de"
}
Response headers
 connection: keep-alive 
 content-type: application/json 
 date: Tue,11 Feb 2025 03:23:42 GMT 
 keep-alive: timeout=60 
 transfer-encoding: chunked 
```
**Get Points**

URL: /receipts/{id}/points

Method: GET

```
	
Response body
{
  "points": 28
}
Response headers
 connection: keep-alive 
 content-type: application/json 
 date: Tue,11 Feb 2025 03:23:53 GMT 
 keep-alive: timeout=60 
 transfer-encoding: chunked 
```