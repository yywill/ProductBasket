
# Product Basket Restful API

This project implements a Restful API for an electronics store's checkout system.

## Features

- Multiple Admin endpoints
    - Create / Update / Delete Products
    - Create / Update / Delete Discounts for Products
- Customer endpoints
    - Add / Remove Products to basket
    - Calculate total price of basket including discounts
- Entity models for Products, Discounts and Baskets
- In memory H2 database
- Unit tests
- Test failures cause build failures

## Technologies

- Spring Boot
- Spring Data JPA
- Spring Rest
- Lombok
- H2 Database
- JUnit

## Usage

### Running the application

```
mvn spring-boot:run
```

### Running tests

```
mvn test
```

### Database Credentials

```
JDBC URL: jdbc:h2:mem:testdb  
User Name: sa
```
### Run Complied Jar

```shell
java -jar -Dspring.profiles.active=testnet bullish-williamyang-project.jar
```   

The API will be available at `http://localhost:8080`.

HINT: to look at H2 Database, visit
```shell
http://localhost:8080/h2-console/
```

To login:

```yaml
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
```

### Admin Endpoints

- Products:
  - GET /api/products
    - Get all products with pagination. Pass:
      - `?limit=20` to get 20 products per page
      - `?cursor=4` to get the next page after product id 4
         ``` 
         /api/products?limit=20&cursor=4
         ```
    - Examples:
      - First page (products 1-20):
        ```
        /api/products?limit=20
        ```  
      - Second page (products 21-40):
        ```
        /api/products?limit=20&cursor=20
        ```  
  - POST /api/products
    - Create a new product
  - DELETE /api/products/{id}
    - Delete a product by id

- Discounts
  - GET /api/discounts
    - Get all discounts with pagination. Uses same parameters as /products.
  - POST /api/discounts/product/{productId}
  - PUT /api/discounts/product/{productId}/discount/{id}
  - DELETE /api/discounts/{id}

### Customer(Basket) Endpoints

- Create basket: `POST /api/baskets`
- Get basket by code: `GET /api/baskets/{code}`
- Get all baskets: `GET /api/baskets`
- Add item to basket: `POST /api/baskets/{basketCode}/items`
- Update basket item: `PUT /api/baskets/{basketCode}/items/{itemId}`
- Delete basket item: `DELETE /api/baskets/{basketCode}/items/{itemId}`
- Get basket item: `GET /api/baskets/{basketCode}/items/{itemId}`
- Checkout basket: `POST /api/baskets/{basketCode}/checkout`

The checkout endpoint requires an `api-key` header with the customer's API key.

On checkout, a receipt is generated and returned with:

- Customer info
- Date
- Total items
- Total quantity
- List of receipt items
- Total price

## Integration test by Pytest

The intergration test is written by Pytest. It will test the API endpoints and the database.
[see more](pytest/README.md)