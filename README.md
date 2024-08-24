# Dota Web Shop Application

This project is a web shop application developed using Spring Boot. It provides essential features for managing products, processing orders, handling newsletters, and contact forms.

## Features

- **Product Management:** Create, read, update, and delete (CRUD) operations for managing products.
- **Order Processing:** Users can place orders, and the system processes them, checking for item availability and sending confirmation emails.
- **Newsletter Management:** Users can subscribe and unsubscribe from newsletters.
- **Contact Form:** Allows users to send messages through a contact form, with email notifications.

## Technologies Used

- **Java 17**
- **Spring Boot 3.1.9**
  - Spring Data JPA
  - Spring Web
  - Spring Security
  - Spring Mail
  - Spring Thymeleaf
  - Spring Cache
  - Spring Validation
- **MySQL** as the database
- **Flyway** for database migrations
- **Lombok** for reducing boilerplate code
- **MapStruct** for object mapping
- **AWS SDK (S3)** for file storage
- **Apache POI** for handling Microsoft Excel files
- **Caffeine** for caching

## REST API Endpoints

### Products
- `GET /products` - Retrieve all products
- `GET /products/page` - Retrieve products with pagination
- `GET /products/{id}` - Retrieve a product by ID
- `POST /products` - Add a new product
- `DELETE /products/{id}` - Delete a product

### Orders
- `POST /orders` - Create a new order
- `DELETE /orders` - Delete an order by ID

### Newsletter
- `POST /newsletter` - Subscribe to the newsletter
- `GET /newsletter/unsubscribe/{uuid}` - Unsubscribe from the newsletter

### Contact Form
- `POST /contact` - Send a message through the contact form

### **Application Overview:**

#### **Docker Setup**
- The application includes a `Dockerfile` and can be run alongside a MySQL database using a `docker-compose.yml` file.
- The Dockerfile is based on `openjdk:17-jdk-alpine`.
- The database service uses `mysql:8.0` image.

#### **Application Configuration (`application.yml`):**
- **Database Configuration:**
  - Uses MySQL with environment variables for `DB_URL`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`.
  - Caching is enabled with Caffeine.

- **Email Configuration:**
  - Configured for Gmail SMTP with STARTTLS enabled.
  - Custom mail subjects for order notifications, contact form messages, and newsletter operations.

- **Server Configuration:**
  - Runs on a port defined by the `PORT` environment variable (default 8080).
  - The context path is set to `/dota`.

- **AWS S3 Configuration:**
  - Stores images in a bucket defined by `AWS_BUCKET_NAME`.

#### **Environment Variables:**
- Environment variables such as database credentials and email settings are stored in `.env` files that are not included in the repository to protect sensitive data.

#### **Docker Compose Setup:**
- Defines services for MySQL and the application:
  - **MySQL Service:**
    - Exposes port 3306 and includes a health check.
    - Data is persisted using Docker volumes.
  - **Application Service:**
    - Exposes port 8080 and depends on the MySQL service being healthy.
    - Uses environment variables from `.env_dota`.
