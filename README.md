Here is a possible README.md for the project:

# Product Basket Project

This project implements the backend for an electronics store's checkout system.

## Features

- REST API for managing products and baskets
- Entity models for products, discounts and baskets
- Spring Data JPA repositories
- Unit tests
- Swagger UI for API documentation

## Technologies

- Spring Boot
- Spring Data JPA
- H2 in-memory database
- Lombok
- JUnit / Mockito
- Maven

## Usage

### Running the application

```
mvn spring-boot:run
```

The API will be available at http://localhost:8080.

Swagger UI available at http://localhost:8080/swagger-ui.html

### Running tests

```
mvn test
```

### REST API

#### Products

- GET /api/products - Get all products
- POST /api/products - Create a new product
- DELETE /api/products/{id} - Delete a product

#### Baskets

- GET /api/baskets - Get all baskets
- POST /api/baskets - Create a new basket
- PUT /api/baskets/{id} - Add item to basket

See Swagger UI for full API details.

Hope this README captures the important details about the project! Let me know if you have any questions or suggestions for improvements.