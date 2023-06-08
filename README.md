
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

### Compile it to all-in-one Jar 

```
mvn clean package
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

The `test_checkout_basket()` test uses the default API key:

`API-KEY-123456`

### t_customers Table

| id | user_name | client_display_name | api_key          |
| -- | -- | -- |-- |
| 1  | yywill | William Yang | API-KEY-123456|
| 2  | yy | Yang Yang | API-KEY-789012|

This table contains customer information:

- `id` - Auto incrementing primary key
- `user_name` - The customer's username
- `client_display_name` - The customer's full name
- `api_key` - The customer's API key

Two sample customers were inserted:

- William Yang with API key `API-KEY-123456`
- Yang Yang with API key `API-KEY-789012`

This API key is required in the `api-key` header to checkout a basket:

```python
headers = {
    "api-key": "API-KEY-123456" 
}
response = requests.post(url, headers=headers)
```

Hope this helps clarify! Let me know if you have any other questions.



On checkout, a receipt is generated and returned with:

- Customer info
- Date
- Total items
- Total quantity
- List of receipt items
- Total price


# Test Driven Design

## Unit Tests

### Domain Tests

- Uses @SpringBootTest to boot up the full application context
- Tests the JPA repositories directly
- No mocking involved, uses the actual database

Examples:

- `RepoTest`
  - Tests saving and finding products, baskets and discounts
  - Uses the product, basket and discount repositories

### Controller Tests

- Uses @SpringBootTest and @AutoConfigureMockMvc
- Boots up only the web layer, mocks dependencies
- Tests the actual controller layer
- No interaction with the database

Examples:

- `ProductControllerTest`
  - Mocks the product repository
  - Tests the different product endpoints
  - Asserts the HTTP status codes and response bodies

- `DiscountControllerTest`
  - Mocks the discount and product repositories
  - Injects the controller using @InjectMocks
  - Tests all discount endpoints

- `BasketControllerTest`
  - Mocks the basket, customer and product repositories
  - Tests creating, updating and deleting basket items
  - Tests checking out a basket and verifying the receipt

Key Points:

- Domain tests are faster since they interact with the actual database
- Controller tests are isolated since they mock dependencies
- Ideally you want both types of tests to ensure full coverage

## Integration test by Pytest

The intergration test is written by Pytest. It will test the API endpoints and the database.
[see more](pytest/README.md)