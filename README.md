# GastroManager
The GastroManager is a restaurant management application that is designed to streamline and enhance the operational efficiency of restaurants. This web-based solution provides restaurant owners and staff with tools to manage inventory, place orders, and optimize kitchen operations seamlessly.

# Project Title:
Restaurant Management System GastroManager

# Problem Statement:
Managing restaurant operations like inventory control, menu updates, and sales reporting can be inefficient when handled manually. Inaccuracies in stock levels, difficulty tracking ingredient usage, and a lack of insightful reporting can lead to resource wastage and operational delays. This project aims to provide an integrated system to streamline these tasks, enhancing overall restaurant efficiency.

# Brief Overview of the Application’s Functionality:
The Restaurant Management System is a web-based application designed to manage restaurant operations such as inventory tracking, menu management, orders placing and sales reporting. It simplifies daily operations by automatically updating inventory levels when orders are placed and providing detailed performance reports. The system will also ensure secure access through role-based permissions with JWT authentication.

# Technology Stack:
- **Frontend**: Angular for a dynamic, user-friendly interface.
- **Backend**: Java and Spring Boot for server-side logic and REST API development.
- **Database**: PostgreSQL for efficient and scalable data management.
- **Security**: JWT (JSON Web Token) for user authentication and role-based access control.
- **Containerization**: Docker for creating consistent environments for development and deployment.
- **Version Control**: Git and GitHub for managing source code and version control.

# Features to Be Implemented:

- **Inventory Management:**
  - Automatically deduct ingredient quantities from inventory when a menu item is ordered.
  - Set alerts for low stock levels and generate inventory usage reports (not in first MVP).

- **Menu Management:**
  - Add, update, and remove menu items, including specifying ingredients, categories and prices.
  - Ensure that menu item availability is updated based on current inventory levels (not in first MVP).

- **Orders Management:**
  - Create order for the clients including the menu items and its quantities for each order, and automatically substract the quantities for each ingredient from the inventory.

- **Sales and Reporting:**

  - Generate sales reports based on daily, weekly, or monthly data.
  - Provide insights into popular dishes and track ingredient costs for better decision-making (not in first MVP).

- **User Role Management:**
  - Implement different user roles (e.g., owner, admin, waitress, etc).
  - Secure the application using JWT for authentication, with role-based permissions to control access to features.

# User stories

The user stories (opened and closed) for this project can be found here: [GitHub User Stories](https://github.com/CarlosBrrs/GastroManager/issues?q=)

# Design & API Contract

## High level architecture

![High level architecture](./GastroManager.drawio.png)

## API Contracts

The requests to the API are going to be a json object, and the schema will depend on each request, which is defined below.

The response of the API is in the same schema whether it is a successfull response or not: 
```json
{
  "timestamp": string,
  "flag":boolean,
  "message": string,
  "data": Object
}
```
### Ingredients management

#### GET /api/v1/ingredients (View Inventory)
- **Description**: Retrieve a list of ingredients in the inventory
- **Endpoint**: /api/v1/ingredients
- **Method**: GET
- **Request Body**: None
- **Response Body (200 OK):**
```json
{
  "timestamp": "2024-10-08T10:00:00Z",
  "flag": true,
  "message": "Inventory retrieved successfully",
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "Tomato",
      "stockLevel": 1000,
      "unit": "g",
      "pricePerUnit": 0.5,
      "lastUpdated": "2024-10-08T10:00:00Z"
    },
    {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "name": "Olive Oil",
      "stockLevel": 500,
      "unit": "mL",
      "pricePerUnit": 1.0,
      "lastUpdated": "2024-10-08T09:30:00Z"
    }
  ]
}

```

#### GET /api/v1/ingredients/{ingredientId} (View Ingredient by ID)
- **Description**: Retrieve a specific ingredient's details by ID.
- **Endpoint**: /api/v1/ingredients/{ingredientId}
- **Method**: GET
- **Request Body**: None
- **Response Body (200 OK):**
```json
{
  "timestamp": "2024-10-08T10:00:00Z",
  "flag": true,
  "message": "Ingredient retrieved successfully",
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Tomato",
    "stockLevel": 1000,
    "unit": "g",
    "pricePerUnit": 0.5,
    "lastUpdated": "2024-10-08T10:00:00Z"
  }
}

```

#### POST /api/v1/ingredients (Add New Ingredient)
- **Description**: Add a new ingredient to the inventory.
- **Endpoint**: /api/v1/ingredients
- **Method**: POST
- **Request Body**:
```json
{
  "name": "Tomato",
  "stockLevel": 1000,
  "unit": "g",
  "pricePerUnit": 0.5
}


```
- **Response Body (201 CREATED):**
```json
{
  "timestamp": "2024-10-08T12:00:00Z",
  "flag": true,
  "message": "Ingredient added successfully",
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Tomato",
    "stockLevel": 1000,
    "unit": "g",
    "pricePerUnit": 0.5,
    "lastUpdated": "2024-10-08T12:00:00Z"
  }
}

```

#### PUT /api/v1/ingredients/{ingredientId} (Update Stock Level)
- **Description**:  Update details of an existing ingredient, including stock level, name, unit, price per unit, and update reason.
- **Endpoint**: /api/v1/ingredients/{ingredientId}
- **Method**: PUT
- **Request Body**:
```json
{
  "name": "Tomato",
  "stockLevel": 1000,
  "unit": "g",
  "pricePerUnit": 0.50,
  "updateReason": "Received new shipment"
}

```
- **Response Body (200 OK):**
```json
{
  "timestamp": "2024-10-08T14:00:00Z",
  "flag": true,
  "message": "Ingredient updated successfully",
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "Tomato",
    "stockLevel": 1000,
    "unit": "g",
    "pricePerUnit": 0.50,
    "lastUpdated": "2024-10-08T14:00:00Z",
    "updatedBy": "manager@example.com",
    "updateReason": "Received new shipment"
  }
}

```
