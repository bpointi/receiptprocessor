# Getting Started

This is a Spring Boot application that calculates 
the points for a given receipt. It is dockerized and its API endpoints may be demonstrated 
by running its Docker container and sending cURL requests.
## How to Run Project

1. Download the project and start your Docker daemon. You can do this by opening the Docker Desktop application on your system
2. While running Docker, build the Docker image by navigating to the root of the project directory and executing in your terminal:
```agsl
docker build -t receiptprocessor/src .
```
3. Spin-up a container from the image with:
```agsl
docker run -p 8080:8080 receiptprocessor/src
```
From your current terminal window, you may view the logging results outputted from Spring Logger. 

4. Open a new terminal window and obtain the identifier of the container with:
```agsl
docker container ls
```
This should yield
```agsl
CONTAINER ID   IMAGE                  COMMAND                  CREATED          STATUS          PORTS                    NAMES
1d184f14e096   receiptprocessor/src   "java -jar /receiptp…"   36 seconds ago   Up 35 seconds   0.0.0.0:8080->8080/tcp   nervous_lehmann
```
Keep track of the Container ID that is outputted in your terminal. You will need this to send requests from the terminal.

5. You may now send cURL requests to the API from within the container. Here are some POST and GET requests that can be made:
### POST 
```agsl
docker exec -it <container_id>  sh -c 'curl -i -X POST -H "Content-Type: application/json" -d '\''{
  "retailer": "M&M Corner Market",
  "purchaseDate": "2022-03-20",
  "purchaseTime": "14:33",
  "items": [
    {
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    },{
      "shortDescription": "Gatorade",
      "price": "2.25"
    }
  ],
  "total": "9.00"
}'\'' http://localhost:8080/receipts/process'
```
```agsl
docker exec -it <container_id>  sh -c 'curl -i -X POST -H "Content-Type: application/json" -d '\''{
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
}'\'' http://localhost:8080/receipts/process'

```
Sample Output
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 01:11:52 GMT

{"id":1}%              
```
### GET
```agsl
docker exec -it <container_id> sh -c 'curl -i http://localhost:8080/receipts/1/points'
```
```agsl
docker exec -it <container_id> sh -c 'curl -i http://localhost:8080/receipts/2/points'
```
Sample Output
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 30 Sep 2023 01:22:35 GMT

{"points":109}%                                       
```
---
## Summary of API Specification

### Endpoint: Process Receipts

* Path: `/receipts/process`
* Method: `POST`
* Payload: Receipt JSON
* Response: JSON containing an id for the receipt.

Description:

Takes in a JSON receipt (see example in the example directory) and returns a JSON object with an ID generated by your code.

The ID returned is the ID that should be passed into `/receipts/{id}/points` to get the number of points the receipt
was awarded.

Example Response:
```json
{ "id": 1 }
```

## Endpoint: Get Points

* Path: `/receipts/{id}/points`
* Method: `GET`
* Response: A JSON object containing the number of points awarded.

A simple Getter endpoint that looks up the receipt by the ID and returns an object specifying the points awarded.

Example Response:
```json
{ "points": 32 }
```
