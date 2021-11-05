# The credit line

This is a microservice for reviewing credit applications from customers, this project is written in Java 11

### How to run the application

It is required to have the version 11 of the JDK & JRE in your local environment.
they can be installed from here:

* [Install AdoptOpenJdk](https://adoptopenjdk.net/installation.html?variant=openjdk11&jvmVariant=hotspot#)

Once installed you have yo run the following commands from your terminal previously located at the project root directory


* If you are using MacOs/Linux use

    To build the project run: `./mvnw clean install`
    
    To run the application execute: `./mvnw spring-boot:run`

* If you are on Windows, use the one with "cmd" extension `mvnw.cmd`



after having the application running you can call the endpoint

[http://localhost:8080](http://localhost:8080)
* [/credits/applications](http://localhost:8080/credits/applications)

whith the following JSON payload example


```json
{
  "cashBalance": 435.3,
  "foundingType": "SME",
  "monthlyRevenue": 4235.45,
  "requestedCreditLine": 100,
  "requestedDate": "2021-07-19T16:32:59.860Z"
}
```


To get a response as the following


```json
{
    "creditLineStatus": "ACCEPTED",
    "creditLine": 100.0,
    "message": "The credit line application is approved"
}
```


You can also check the API documentation on :
[Swagger UI - API documentation](http://localhost:8080/swagger-ui)


