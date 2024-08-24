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

### **Setup:**

#### **Environment Variables:**
- Sensitive environment variables are stored in `.env` files, which are not included in the repository. These files must be created and configured before running the application.
  - **`.env_mysql`:** Contains MySQL-specific variables.
    - `MYSQL_ROOT_PASSWORD`
    - `MYSQL_DATABASE`
    - `MYSQL_USER`
    - `MYSQL_PASSWORD`
  - **`.env_dota`:** Contains application-specific variables.
    - `DB_URL`
    - `DB_NAME`
    - `DB_USERNAME`
    - `DB_PASSWORD`
    - `MAIL_USERNAME`
    - `MAIL_PASSWORD`
    - `CUSTOM_TOKEN`
    - `AWS_ACCESS_KEY_ID`
    - `AWS_SECRET_ACCESS_KEY`
    - `AWS_REGION`
    - `AWS_BUCKET_NAME`
    - `AWS_IMAGE_URL`
    - `PROTOCOL`
    - `HOST`
    - `PORT`
    - `ORIGIN_ALLOWED`

#### **Docker Setup:**
- The application is Dockerized and can be run together with a MySQL database using a `docker-compose.yml` file.
- The `Dockerfile` is based on `openjdk:17-jdk-alpine` and builds the application from the `dota-0.0.1-SNAPSHOT.jar`.
- The MySQL service uses the `mysql:8.0` image and is configured with environment variables from `.env_mysql`.
- The application service uses environment variables from `.env_dota` to configure the database connection, email settings, AWS S3, and other custom settings.

#### **Steps to Run the Application:**
1. **Prepare Environment Files:**
   - Create `.env_mysql` and `.env_dota` files in the `docker` directory.
   - Populate these files with the necessary environment variables.

2. **Build and Run with Docker Compose:**
   - Navigate to the project root directory.
   - Run `docker-compose up --build` to build and start the application and MySQL services.

3. **Access the Application:**
   - The application will be accessible at `http://localhost:8080/dota` (or another port if specified in the `.env_dota` file).

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
