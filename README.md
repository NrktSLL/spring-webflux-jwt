# WEBFLUX JWT

> JWT example with Webflux 

<p align="center">
<img src="https://github.com/NrktSLL/spring-webflux-jwt/blob/master/images/spring-webflux-jwt%20.png" alt="SSpring Webflux JWT Example" width="100%" height="100%"/> 
</p>


## Installation
Prerequisites:
*  Docker
*  Docker Compose

>Container creation with [Buildpacks](https://buildpacks.io/) 
```
mvn clean install && docker-compose up -d
```

## Build/Package
```
mvn clean package
```

## Testing
```
mvn test
```

## Used Dependencies
* Spring Boot WebFlux
* Spring Boot Actuator
* Spring Boot Validation
* Spring Boot Data MongoDB Reactive
* Spring Boot Data Redis
* Spring Boot Security
* io.jsonwebtoken [jjwt](https://github.com/jwtk/jjwt) 
* Springdoc Openapi Webflux (openapi-webflux-ui + security)
* [Zalando](https://github.com/zalando/problem-spring-web)  webflux problem
* Mapstruct
* Lombok

## Abilities
* JWT
* Reactive Audit
* Zalando Problem

## Swagger
> **Access : http://localhost:8080/api/documentation/**

<img src="https://github.com/NrktSLL/spring-webflux-jwt/blob/master/images/spring-webflux-jwt-swagger.png" alt="Spring Webflux JWT Swagger" width="100%" height="100%"/> 

## Mongo Express
> **Access : http://localhost:8085/**

## Redis Commander
> **Access : http://localhost:8081/**

