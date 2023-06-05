import requests
import uuid

API_ENDPOINT = "http://localhost:8080/admin/api/discounts"


def test_create_discount():
    # Define the product request body
    product_data = {
        "name": "Test product",
        "description": "Discount product",
        "price": 20.0,
        "currency": "USD"
    }

    # Create the product
    product_url = "http://localhost:8080/admin/api/products"
    response = requests.post(product_url, json=product_data)

    # Assert that the product was created successfully
    assert response.status_code == 201, "fail at creating product" 
    product_id = response.json()["id"]

    # Define the discount request body
    discount = {
        "name": "Test discount",
        "discountPercentage": 10.0
    }

    # Create the discount
    url = f"{API_ENDPOINT}/product/{product_id}"
    response = requests.post(url, json=discount)

    # Assert that the discount was created successfully
    assert response.status_code == 200, "fail at creating discount"
    assert "code" in response.json()


def test_get_all_discounts():
    # Send a GET request to retrieve all discounts
    response = requests.get(API_ENDPOINT)

    # Assert that the response status code is 200 OK
    assert response.status_code == 200

    # Assert that the response body is a list of discounts
    discounts = response.json()
    assert isinstance(discounts, list)
    assert len(discounts) >= 0


def test_get_discount_by_id():
    # Send a GET request to retrieve the first discount by ID
    url = f"{API_ENDPOINT}/1"
    response = requests.get(url)

    # Assert that the response status code is 200 OK
    assert response.status_code == 200

    # Assert that the response body contains the correct discount data
    assert "id" in response.json()


def test_update_discount():
    # Define the updated discount data
    updated_discount = {
        "name": "Updated discount",
        "discountPercentage": 20.0
    }

    # Send a PUT request to update the first discount
    url = f"{API_ENDPOINT}/product/1/discount/1"
    response = requests.put(url, json=updated_discount)

    # Assert that the response status code is 200 OK
    assert response.status_code == 200

    # Assert that the discount was updated correctly
    assert "id" in response.json()


def test_delete_discount():
    # Send a DELETE request to delete the first discount
    url = f"{API_ENDPOINT}/1"
    response = requests.delete(url)

    # Assert that the response status code is 204 No Content
    assert response.status_code == 204