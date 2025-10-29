# Todo List API

A RESTful API for managing todo lists with JWT authentication built with Spring Boot.

## Description

This API provides secure endpoints for creating, reading, updating, and deleting todo items. It includes user authentication and authorization using JWT tokens with access and refresh token mechanisms.


## How to Run

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Database (PostgreSQL/MySQL)

### Setup

* Clone the repository:
```bash
git clone <repository-url>
cd todo-list-api
```


* Build the project:
```bash
mvn clean install
```

* Run the application:
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## How to Use

### Authentication

#### Register a new user
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "user",
  "email": "user@example.com",
  "password": "password123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "password123"
}
```

Response includes `accessToken` and `refreshToken`.

#### Refresh Access Token
```http
POST /api/auth/refresh
Authorization: Bearer <refresh_token>
```

**Note**: Use the refresh token in the Authorization header, not the expired access token.

### Todo Endpoints

All todo endpoints require the access token in the Authorization header:

#### Get all todos
```http
GET /api/todos
Authorization: Bearer <access_token>
```

#### Create a todo
```http
POST /api/todos
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "completed": false
}
```

#### Update a todo
```http
PUT /api/todos/{id}
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "completed": true
}
```

#### Delete a todo
```http
DELETE /api/todos/{id}
Authorization: Bearer <access_token>
```

