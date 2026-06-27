# E-book Management System

A web-based application to search, read, download, and manage e-books, built with
**Spring Boot (MVC)**, **Spring Security**, **Spring Data JPA / Hibernate**, **MySQL**,
**Thymeleaf**, and **Bootstrap 5**.

> Note on versions: the original brief mentioned Spring Boot 4.1.0, which is not yet
> released. This project uses **Spring Boot 3.3.5 on Java 21**, which is current,
> stable, and uses the same Jakarta + Spring Security 6 APIs. It runs as-is today.

---

## Features

**Reader (USER)**
- Register, login, logout
- Search books by title or author, browse by category
- View book details, read PDFs online, download files
- Personal collection (add/remove favorites)

**Admin (ADMIN)**
- Dashboard with counts
- Full CRUD for books (with PDF + cover image upload)
- Full CRUD for categories
- Manage user accounts
- Role-based redirect after login

---

## Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Language     | Java 21                             |
| Framework    | Spring Boot 3.3.5 (Spring MVC)      |
| Security     | Spring Security 6 + BCrypt          |
| Persistence  | Spring Data JPA / Hibernate         |
| Database     | MySQL 8                             |
| View         | Thymeleaf + Bootstrap 5             |
| Build        | Maven                               |

---

## Prerequisites

1. **JDK 21+** — verify with `java -version`
2. **Maven 3.9+** — verify with `mvn -version` (or use the bundled IDE Maven)
3. **MySQL 8** running locally on port `3306`

---

## Configuration

Open `src/main/resources/application.properties` and set your MySQL credentials:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

The database `ebook_management_system` is created automatically on first run
(`createDatabaseIfNotExist=true`), and Hibernate creates all tables.

---

## Running the Project

### Option A — Command line
```bash
mvn spring-boot:run
```

### Option B — IntelliJ IDEA
1. **File > Open** and select the project folder (it detects `pom.xml`).
2. Wait for Maven to import dependencies.
3. Run `EbookManagementSystemApplication`.

### Option C — VS Code
1. Install the **Extension Pack for Java** and **Spring Boot Extension Pack**.
2. Open the folder, then **Run** `EbookManagementSystemApplication`.

Then open: **http://localhost:8080**

---

## Design Patterns & Principles

This project implements six design patterns (Singleton, Simple Factory, Prototype,
Command, Adapter, Facade) and applies SOLID + clean-code principles. Every pattern
is mapped to its exact file and route in **`DESIGN_PATTERNS.md`** — see that file for
the full grading checklist and a runtime demo script. All pattern classes live under
`src/main/java/com/ebookmanagement/pattern/`.

---

## Default Admin Account

Created automatically on first startup:

```
Email:    admin@ebook.com
Password: admin123
```

Log in as admin to add categories and books. Register a separate account to test the
reader experience.

---

## File Uploads

Uploaded files are stored on disk (only their **paths** are saved in the database):

```
uploads/books/    <- PDF / e-book files
uploads/covers/   <- cover images
```

Cover images are served at `/covers/**`. Book PDFs are served only through the
secured `/books/read/{id}` and `/books/download/{id}` endpoints (login required).

---

## Project Structure

```
com.ebookmanagement
├── config          # SecurityConfig, WebConfig, DataInitializer
├── controller      # MVC controllers
├── dto             # Form-backing objects + validation
├── entity          # JPA entities (User, Book, Category, CollectionItem)
├── exception       # Custom exceptions + global handler
├── repository      # Spring Data JPA repositories
├── security        # UserDetailsService, login success handler
└── service / impl  # Business logic
```

---

## Public vs Protected Routes

| Route pattern                                   | Access            |
|-------------------------------------------------|-------------------|
| `/`, `/books`, `/books/search`, `/books/{id}`   | Public            |
| `/login`, `/register`, `/css/**`, `/js/**`      | Public            |
| `/user/**`, `/collection/**`, `/books/read/**`, `/books/download/**` | USER or ADMIN |
| `/admin/**`                                     | ADMIN only        |
