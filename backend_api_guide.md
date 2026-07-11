# Dealership Inventory System — Backend API Guide

The backend is now **completely implemented**. 66/66 test cases (including database repositories, security, services, controllers, and end-to-end integration flows) are passing successfully.

Below is the complete API reference followed by instructions on how to test them using **Postman**.

---

## 1. API Reference

All endpoints are prefixed with `/api`.

### A. Authentication Endpoints

#### 1. Register User
* **Endpoint**: `POST /api/auth/register`
* **Access**: Public
* **Request Body** (JSON):
  ```json
  {
    "email": "customer1@route66.com",
    "password": "customerPassword",
    "name": "Sally Carrera"
  }
  ```
* **Response (HTTP 201 Created)**:
  * *Sets httpOnly Cookie*: `jwt=<token_value>; Path=/; HttpOnly`
  * *Response Body*:
    ```json
    {
      "token": "eyJhbGciOi...",
      "email": "customer1@route66.com",
      "name": "Sally Carrera",
      "role": "USER"
    }
    ```

#### 2. Login User
* **Endpoint**: `POST /api/auth/login`
* **Access**: Public
* **Request Body** (JSON):
  ```json
  {
    "email": "admin@cardealership.com",
    "password": "Admin@123"
  }
  ```
* **Response (HTTP 200 OK)**:
  * *Sets httpOnly Cookie*: `jwt=<token_value>; Path=/; HttpOnly`
  * *Response Body*:
    ```json
    {
      "token": "eyJhbGciOi...",
      "email": "admin@cardealership.com",
      "name": "Admin",
      "role": "ADMIN"
    }
    ```

#### 3. Current Profile (/me)
* **Endpoint**: `GET /api/auth/me`
* **Access**: Authenticated (Requires active session cookie)
* **Response (HTTP 200 OK)**:
  ```json
  {
    "email": "customer1@route66.com",
    "name": "Sally Carrera",
    "role": "USER"
  }
  ```

#### 4. Logout User
* **Endpoint**: `POST /api/auth/logout`
* **Access**: Public (Destroys active session cookie)
* **Response (HTTP 200 OK)**:
  * *Clears Cookie*: `jwt=; Path=/; Max-Age=0; HttpOnly`
  * *Response Body*:
    ```json
    {
      "message": "Logged out successfully"
    }
    ```

---

### B. Vehicle Catalog Endpoints

#### 1. Get All Vehicles
* **Endpoint**: `GET /api/vehicles`
* **Access**: Authenticated
* **Response (HTTP 200 OK)**:
  ```json
  [
    {
      "id": 1,
      "make": "Lightning",
      "model": "McQueen Special",
      "category": "COUPE",
      "price": 95000.00,
      "quantity": 5,
      "year": 2024,
      "description": "Ka-Chow! Speed. I am speed.",
      "imageUrl": null,
      "createdAt": "2026-07-11T12:00:00",
      "updatedAt": "2026-07-11T12:00:00"
    }
  ]
  ```

#### 2. Get Vehicle by ID
* **Endpoint**: `GET /api/vehicles/{id}`
* **Access**: Authenticated
* **Response (HTTP 200 OK)**:
  ```json
  {
    "id": 1,
    "make": "Lightning",
    "model": "McQueen Special",
    "category": "COUPE",
    "price": 95000.00,
    "quantity": 5,
    "year": 2024,
    "description": "Ka-Chow! Speed. I am speed.",
    "imageUrl": null,
    "createdAt": "2026-07-11T12:00:00",
    "updatedAt": "2026-07-11T12:00:00"
  }
  ```

#### 3. Search and Filter Vehicles
* **Endpoint**: `GET /api/vehicles/search`
* **Access**: Authenticated
* **Query Parameters** (All optional):
  * `make` (string, case-insensitive partial match)
  * `model` (string, case-insensitive partial match)
  * `category` (enum: `SEDAN`, `SUV`, `TRUCK`, `COUPE`, `CONVERTIBLE`, `HATCHBACK`, `VAN`, `ELECTRIC`)
  * `minPrice` (decimal)
  * `maxPrice` (decimal)
* **Example Request**: `GET /api/vehicles/search?make=light&minPrice=80000`
* **Response (HTTP 200 OK)**: A list of matching vehicles in JSON format.

#### 4. Add New Vehicle
* **Endpoint**: `POST /api/vehicles`
* **Access**: Authenticated
* **Request Body** (JSON):
  ```json
  {
    "make": "Doc",
    "model": "Hudson Hornet",
    "category": "SEDAN",
    "price": 45000.00,
    "quantity": 3,
    "year": 1951,
    "description": "The Fabulous Hudson Hornet.",
    "imageUrl": "https://example.com/hudson.png"
  }
  ```
* **Response (HTTP 201 Created)**: Returns the saved vehicle response containing the assigned `id`.

#### 5. Update Vehicle
* **Endpoint**: `PUT /api/vehicles/{id}`
* **Access**: Authenticated
* **Request Body** (JSON): Same structure as Create Vehicle.
* **Response (HTTP 200 OK)**: Returns the updated vehicle details.

#### 6. Delete Vehicle
* **Endpoint**: `DELETE /api/vehicles/{id}`
* **Access**: **Admin Only** (Fails with `403 Forbidden` if regular user attempts)
* **Response (HTTP 204 No Content)**: Returns empty response.

---

### C. Inventory & Transaction Endpoints

#### 1. Purchase Vehicle
* **Endpoint**: `POST /api/vehicles/{id}/purchase`
* **Access**: Authenticated
* **Response (HTTP 200 OK)**:
  * *Decrements vehicle stock quantity by 1*
  * *Response Body*:
    ```json
    {
      "purchaseId": 1,
      "buyerEmail": "customer1@route66.com",
      "buyerName": "Sally Carrera",
      "vehicleId": 1,
      "vehicleDetails": "Lightning McQueen Special",
      "quantity": 1,
      "totalPrice": 95000.00,
      "purchasedAt": "2026-07-11T12:35:10"
    }
    ```
* **Response (HTTP 409 Conflict)**: Returned if the vehicle's quantity is `0` (Out of Stock).

#### 2. Restock Vehicle
* **Endpoint**: `POST /api/vehicles/{id}/restock`
* **Access**: **Admin Only** (Fails with `403 Forbidden` if regular user attempts)
* **Request Body** (JSON):
  ```json
  {
    "quantity": 10
  }
  ```
* **Response (HTTP 200 OK)**:
  * *Increments vehicle stock quantity by request amount*
  * *Response Body*: Returns updated vehicle details.

---

## 2. Testing Steps in Postman

Because our system uses **`httpOnly` session cookies** to store JWT tokens, Postman manages the authentication seamlessly. Whenever you register or log in, Postman automatically extracts the cookie from the response headers and attaches it to subsequent requests.

### Step 1: Run the Spring Boot Server
In your PowerShell terminal, run:
```bash
mvn spring-boot:run
```
*(The automatic env-loader will boot Postgres on port 5433 or 5432 depending on your `.env` settings).*

---

### Step 2: Test User Registration
Let's register a new customer user.
1. Open Postman and create a new request tab.
2. Set the method to **`POST`** and enter the URL:
   `http://localhost:8080/api/auth/register`
3. Click the **Body** tab, select **raw**, select **JSON**, and paste this payload:
   ```json
   {
     "email": "mater_fan@radiator.com",
     "password": "customerPassword",
     "name": "Tow Mater Fan"
   }
   ```
4. Click **Send**.
5. You should receive a `201 Created` status code. In the response, look at the **Cookies** tab at the bottom—you should see a cookie named `jwt`.

---

### Step 3: Verify the Authenticated Session (/me)
Let's see if we are recognized as the logged-in user.
1. Create a new request tab.
2. Set the method to **`GET`** and URL to:
   `http://localhost:8080/api/auth/me`
3. Click **Send**.
4. You should see a `200 OK` response returning the details of the registered user (`mater_fan@radiator.com`). Postman automatically sent the `jwt` cookie.

---

### Step 4: Test Vehicle Catalog Access
1. Set the method to **`GET`** and URL to:
   `http://localhost:8080/api/vehicles`
2. Click **Send**.
3. You should see a `200 OK` response with the 18 seeded vehicles.

---

### Step 5: Test Search and Filter
1. Set the method to **`GET`** and URL to:
   `http://localhost:8080/api/vehicles/search?make=Lightning`
2. Click **Send**.
3. You should only see McQueen vehicles in the response array.

---

### Step 6: Test Vehicle Purchase (USER role)
Let's purchase McQueen's car (ID 1).
1. Set the method to **`POST`** and URL to:
   `http://localhost:8080/api/vehicles/1/purchase`
2. Click **Send**.
3. You should get a `200 OK` response with the transaction details under `buyerEmail: mater_fan@radiator.com`. The vehicle quantity in the DB drops by 1.

---

### Step 7: Test Admin-Only Restock Restriction
Let's see if a normal user is blocked from restocking.
1. Set the method to **`POST`** and URL to:
   `http://localhost:8080/api/vehicles/1/restock`
2. Under the **Body** tab (raw, JSON), enter:
   ```json
   {
     "quantity": 10
   }
   ```
3. Click **Send**.
4. You should receive an **`403 Forbidden`** status because `mater_fan@radiator.com` only has the `USER` role.

---

### Step 8: Log in as Admin
Let's switch to the administrator account.
1. Set the method to **`POST`** and URL to:
   `http://localhost:8080/api/auth/login`
2. Under the **Body** tab (raw, JSON), enter the seeded admin credentials:
   ```json
   {
     "email": "admin@cardealership.com",
     "password": "Admin@123"
   }
   ```
3. Click **Send**.
4. You should receive a `200 OK` response, and Postman will update the `jwt` cookie with the Admin's token.

---

### Step 9: Test Admin Restock (Succeeds)
Now that you are logged in as admin:
1. Go back to your restock request (`POST http://localhost:8080/api/vehicles/1/restock`).
2. Click **Send**.
3. You should now receive a **`200 OK`** response returning the vehicle details with the quantity successfully incremented by 10!

---

### Step 10: Test Logout
1. Set the method to **`POST`** and URL to:
   `http://localhost:8080/api/auth/logout`
2. Click **Send**.
3. The cookie will be cleared. If you try to call `GET /api/auth/me` or check the catalog again, you will receive a **`401 Unauthorized`** response.
