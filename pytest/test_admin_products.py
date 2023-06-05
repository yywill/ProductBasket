import requests
import uuid

API_ENDPOINT = "http://localhost:8080/admin/api/products"

def test_create_product():
    # Generate a random product name
    name = f"Product {uuid.uuid4().hex[:8]}"

    # Define the request body
    payload = {
        "name": name,
        "description": "Test product",
        "price": 10.0,
        "currency": "USD"
    }

    # Send a POST request to create the product
    response = requests.post(API_ENDPOINT, json=payload)

    # Assert that the response status code is 201 Created
    assert response.status_code == 201

    # Assert that the response body contains the new product ID
    product_id = response.json()["id"]
    assert product_id is not None

def test_get_all_products():
    # Send a GET request to retrieve all products
    response = requests.get(API_ENDPOINT)

    # Assert that the response status code is 200 OK
    assert response.status_code == 200

    # Assert that the response body is a list of products
    products = response.json()
    assert isinstance(products, list)
    assert len(products) > 0

def test_delete_product():
    # Create a product to delete
    name = f"Product {uuid.uuid4().hex[:8]}"
    payload = {
        "name": name,
        "description": "Test product",
        "price": 10.0,
        "currency": "USD"
    }
    response = requests.post(API_ENDPOINT, json=payload)
    product_id = response.json()["id"]

    # Send a DELETE request to delete the product
    delete_url = f"{API_ENDPOINT}/{product_id}"
    response = requests.delete(delete_url)

    # Assert that the response status code is 204 No Content
    assert response.status_code == 204