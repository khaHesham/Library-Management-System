# Library Management System

This project is a Library Management System API built using Spring Boot. It provides functionality for managing books, patrons, and borrowing records with JWT-based authorization and additional features like logging, caching, and transaction management.

## Features

- **CRUD Operations for Books and Patrons**: API endpoints for managing books and patrons.
- **Borrowing Records**: Tracks borrowing and return records of books by patrons.
- **Validation and Error Handling**: Input validation with graceful exception handling.
- **JWT Authorization**: Secures API endpoints with JWT tokens. Admin passwords are stored encoded in the database.
- **Aspect-Oriented Programming (AOP) Logging**: Logs method calls, exceptions, and performance metrics.
- **Caching**: Uses Spring's caching mechanisms for frequently accessed data.
- **Declarative Transaction Management**: Ensures data integrity during critical operations using `@Transactional`.

## Prerequisites

- Java 22
- Maven 3.8+
- Docker (for containerization)
- PostgreSQL

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

### 2. Configure PostgreSQL

Ensure PostgreSQL is installed and running. Create a database for the application:

```sql
CREATE DATABASE LibrarySystemDB;
```

### 3. Update Configuration

Update `src/main/resources/application.properties` with your PostgreSQL connection details:

```properties
spring.application.name=API
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/LibrarySystemDB
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.database=postgresql
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Running the Application

#### Using Docker

Create a Docker image:

```bash
docker build -t library-management-system .
```

Run the Docker container:

```bash
docker run -d -p 8080:8080 --name library-management-system library-management-system
```

#### Without Docker

```bash
mvn spring-boot:run
```

### 6. Register an Admin User

Before interacting with the API, register an admin user. Use the `/api/auth/register` endpoint. The password will be encoded and stored in the database.

Example cURL command to register an admin:

```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username": "admin", "password": "adminpassword"}'
```

### 7. Obtain a JWT Token

Use the `/api/auth/login` endpoint to obtain a JWT token.

Example cURL command to log in and get a token:

```bash
curl -X POST http://localhost:8080/api/auth/authenticate -H "Content-Type: application/json" -d '{"username": "admin", "password": "adminpassword"}'
```

The response will include a JWT token:

```json
{
  "token": "your.jwt.token.here"
}
```

### 8. Using the JWT Token

Include the token in the `Authorization` header for all API requests:

```bash
curl -H "Authorization: Bearer your.jwt.token.here" http://localhost:8080/api/books
```

### 9. Database Initialization and Seeding

To seed the PostgreSQL database with initial data, use the provided script:

Create a file named `seed-database.sh`:

```bash
#!/bin/bash

echo "Seeding the database..."

psql -U postgres -d LibrarySystemDB -c "INSERT INTO book (title, author, publication_year, isbn) VALUES ('Book 1', 'Author 1', 2023, '1111111111');"
psql -U postgres -d LibrarySystemDB -c "INSERT INTO patron (name, email, phone_number, address, city, state, zip_code) VALUES ('Patron 1', 'patron1@example.com', '123-456-7890', '123 Main St', 'Anytown', 'Anystate', '12345');"

echo "Database seeded successfully."
```

Make the script executable:

```bash
chmod +x seed-database.sh
```

Run the script:

```bash
./seed-database.sh
```

## Project Directory Structure

- `src/main/java/com/librarymanagement/API`: Contains the Java source code for the application.
  - **Entity Classes**: `Admin`, `Book`, `Patron`, `BorrowingRecord`
  - **Controllers**: Handles API requests.
  - **Services**: Contains business logic.
  - **Repositories**: Interfaces for data access.
  - **Configuration**: Security, Caching and application configuration.
  - **Exception:** custom exception handling
  - **Dtos:** enhance security, performance, and maintainability by providing a structured and controlled way of transferring data across different layers or services.
  - **Logging:** AOP logging logic  to log method calls, exceptions, and performance metrics
  - **Mappers:** to map between entity and dto and vice verse in a clean and maintainable way
- `src/main/resources`: Contains configuration files such as `application.properties`.

## API Endpoints

### Admin

* **POST /api/auth/register: register a new admin.**
* **POST /api/auth/authenticate: authenticate admin to access Api endpoints.**

### Book Management

- **GET /api/books**: Retrieve a list of all books.
- **GET /api/books/{id}**: Retrieve details of a specific book by ID.
- **POST /api/books**: Add a new book to the library.
- **PUT /api/books/{id}**: Update an existing book's information.
- **DELETE /api/books/{id}**: Remove a book from the library.

### Patron Management

- **GET /api/patrons**: Retrieve a list of all patrons.
- **GET /api/patrons/{id}**: Retrieve details of a specific patron by ID.
- **POST /api/patrons**: Add a new patron to the system.
- **PUT /api/patrons/{id}**: Update an existing patron's information.
- **DELETE /api/patrons/{id}**: Remove a patron from the system.

### Borrowing Records

- **POST /api/borrow/{bookId}/patron/{patronId}**: Allow a patron to borrow a book.
- **PUT /api/return/{bookId}/patron/{patronId}**: Record the return of a borrowed book by a patron.

## Testing the API

You can test the API using tools like [Postman](https://www.postman.com/) or [cURL](https://curl.se/).

### Example cURL Commands

- **Get all books**:

  ```bash
  curl -H "Authorization: Bearer your.jwt.token.here" -X GET http://localhost:8080/api/books
  ```
- **Add a new book**:

  ```bash
  curl -H "Authorization: Bearer your.jwt.token.here" -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d '{"title": "Book Title", "author": "Author Name", "publicationYear": 2023, "isbn": "1234567890"}'
  ```
