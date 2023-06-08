## Integration test by Pytest

#### test_product.py

This test case will test:

- Creating a product
- Fetching all products
- Deleting a product

##### How to run


Then, to run the test:

```
python test_product.py
```

The test will:

1. Create a sample product
2. Get all products and assert the response
3. Delete the sample product and assert a 204 status code

##### Test functions

`test_create_product()` - Creates a product with a random name, asserting it was created successfully.

`test_get_all_products()` - Gets all products and asserts the response.

`test_delete_product()` - Deletes a product and asserts a 204 No Content response.




#### test_create_discount.py

This test case will:

- Create a product
- Create a discount for that product
- Assert that the discount was created successfully

##### How to run

This test requires the API to be running. You can run the API using:
to run the test:

```
python test_create_discount.py
```

The test will:

1. Create a sample product
2. Get the product ID
3. Create a discount for that product
4. Assert that the discount was created with a code

##### Test functions

`test_create_discount()` - Creates a product and then a discount for that product, asserting the discount was created successfully.

The other test functions in this file test:

`test_get_all_discounts()`
`test_get_discount_by_id()`  
`test_update_discount()`
`test_delete_discount()`

Here is the markdown summary for the basket test case:

#### test_basket.py

This test case will test the Basket API endpoints:

- Creating a basket
- Getting a basket by code
- Adding an item to a basket
- Updating a basket item
- Deleting a basket item
- Checking out a basket

##### How to run

Run the tests: `pytest test_basket.py`

The tests will:

- Create sample baskets
- Add and update basket items
- Checkout a basket and verify the receipt

##### Test functions

- `test_create_basket()` - Creates a basket and asserts it
- `test_get_basket_by_code()` - Gets a basket by code
- `test_add_basket_item()` - Adds an item to a basket
- `test_update_basket_item()` - Updates an existing basket item
- `test_delete_basket_item()` - Deletes a basket item
- `test_checkout_basket()` - Checks out a basket, verifies the receipt and total price

##### Helper function

- `create_product()` - Creates a sample product to use in the tests

