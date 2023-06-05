import uuid
import requests

API_ENDPOINT = "http://localhost:8080/api"

API_ENDPOINT_ADMIN ="http://localhost:8080/admin/api"

def test_create_basket():
    url = f"{API_ENDPOINT}/baskets"
    payload = {
        "name": "Test Basket"
    }
    response = requests.post(url, json=payload)
    assert response.status_code == 200
    basket = response.json()
    assert basket["id"] is not None
    assert basket["code"] is not None

def test_get_basket_by_code():
    url = f"{API_ENDPOINT}/baskets"
    payload = {
        "name": "Test Basket"
    }
    response = requests.post(url, json=payload)
    basket = response.json()
    code = basket["code"]
    url = f"{API_ENDPOINT}/baskets/{code}"
    response = requests.get(url)
    assert response.status_code == 200
    basket = response.json()
    assert basket["code"] == code

def test_get_all_baskets():
    url = f"{API_ENDPOINT}/baskets"
    response = requests.get(url)
    assert response.status_code == 200
    baskets = response.json()
    assert len(baskets) > 0

def create_product():
    url = f"{API_ENDPOINT_ADMIN}/products"
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
    response = requests.post(url, json=payload)

    # Assert that the response status code is 201 Created
    assert response.status_code == 201

    # Assert that the response body contains the new product ID
    product_id = response.json()["id"]
    assert product_id is not None

    # Return the product ID for use in other test cases
    return product_id

def test_add_basket_item():
    # Create a product to use in the test
    product_id = create_product()

    # Create a basket to add the item to
    url = f"{API_ENDPOINT}/baskets"
    payload = {
       
    }
    response = requests.post(url, json=payload)
    basket = response.json()
    basket_code = basket["code"]

    url = f"{API_ENDPOINT}/baskets/{basket_code}/items"
    payload = {
        "product": {"id": product_id},
        "quantity": 2
    }
    response = requests.post(url, json=payload)
    assert response.status_code == 200
    basket_item = response.json()
    assert basket_item["id"] is not None
    assert basket_item["product"]["id"] == product_id
    assert basket_item["quantity"] == payload["quantity"]

def test_update_basket_item():
    # Create a product to use in the test
    product_id = create_product()

    # Create a basket and item to update
    url = f"{API_ENDPOINT}/baskets"
    payload = {
        "name": "Test Basket"
    }
    response = requests.post(url, json=payload)
    basket = response.json()
    basket_code = basket["code"]
    print(f'basket_code {basket_code} created')

    url = f"{API_ENDPOINT}/baskets/{basket_code}/items"
    payload = {
        "product": {"id": product_id},
        "quantity": 2
    }
    response = requests.post(url, json=payload)
    # Assert that the response status code is 201 Created
    assert response.status_code == 200, f"create item failed http_code: {response.status_code}"

    basket_item = response.json()
    basket_item_id = basket_item["id"]
    print(f'basket_item_id {basket_item_id} created')

    # Update the basket item with new information
    payload = {
        "product": {"id": product_id},
        "quantity": 3
    }
    url = f"{API_ENDPOINT}/baskets/{basket_code}/items/{basket_item_id}"
    response = requests.put(url, json=payload)
    assert response.status_code == 200, f'{response.status_code} update basket item failed'

    updated_basket_item = response.json()
    assert updated_basket_item["id"] == basket_item_id
    assert updated_basket_item["product"]["id"] == product_id
    assert updated_basket_item["quantity"] == payload["quantity"]

def test_delete_basket_item():
    # Create a product to use in the test
    product_id = create_product()

    # Create a basket and item to delete
    url = f"{API_ENDPOINT}/baskets"
    payload = {
        "name": "Test Basket"
    }
    response = requests.post(url, json=payload)
    basket = response.json()
    basket_code = basket["code"]

    url = f"{API_ENDPOINT}/baskets/{basket_code}/items"
    payload = {
        "product": {"id": product_id},
        "quantity": 2
    }
    response = requests.post(url, json=payload)
    basket_item = response.json()
    basket_item_id = basket_item["id"]

    # Delete the basket item
    url = f"{API_ENDPOINT}/baskets/{basket_code}/items/{basket_item_id}"
    response = requests.delete(url)
    assert response.status_code == 204

    # Verify that the item has been deleted
    response = requests.get(url)
    assert response.status_code == 404

def test_checkout_basket():
    # Create a product to use in the test
    product_id = create_product()

    # Create a basket to checkout
    url = f"{API_ENDPOINT}/baskets"
    payload = {
        "name": "Test Basket"
    }
    response = requests.post(url, json=payload)
    assert response.status_code == 200
    basket = response.json()
    basket_code = basket["code"]

    # Add items to the basket
    url = f"{API_ENDPOINT}/baskets/{basket_code}/items"
    payload = {
        "product": {"id": product_id},
        "quantity": 2
    }
    response = requests.post(url, json=payload)
    assert response.status_code == 200
    basket_item = response.json()

    # Checkout the basket
    url = f"{API_ENDPOINT}/baskets/{basket_code}/checkout"
    headers = {
        "api-key": "API-KEY-123456"
    }
    response = requests.post(url, headers=headers)
    assert response.status_code == 200
    receipt = response.json()
    assert receipt["clientName"] == "William Yang"
    assert len(receipt["items"]) == 1
    assert receipt["items"][0]["quantity"] == payload["quantity"]
    assert receipt["total"] == 20.0