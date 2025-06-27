# Todo Backend

A simple Todo backend built with **Spring Boot** (Kotlin), secured with Firebase, and containerized with **Docker**.

## Features

- RESTful API for managing todos
- JWT authentication via Firebase
- PostgreSQL for persistent storage
- Docker Compose for easy local and production setup
- MockK and AssertJ for expressive Kotlin testing

## Prerequisites

- [Java 21+](https://adoptopenjdk.net/)
- [Docker](https://www.docker.com/get-started) & [Docker Compose](https://docs.docker.com/compose/)
- (Optional) [Gradle](https://gradle.org/) for local builds

## Getting Started

### 1. Clone the repository

```sh
git clone https://github.com/zabbat/todo_backend.git
cd todo_backend
```

### 2. Configure Environment Variables

Copy the example environment file and edit as needed:

```sh
cp docker/.env.example docker/.env
```

Edit `docker/.env` to set your database credentials and Firebase service account path.

### 3. Build and Run with Docker Compose

```sh
cd docker
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
```

The backend will be available at [https://localhost:443](https://localhost:443) (proxied by NGINX).

### 4. Running Locally (without Docker)

```sh
./gradlew bootRun
```

## Running Tests

```sh
./gradlew test
```

## Project Structure

```
todo_backend/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   └── resources/
│   └── test/
├── docker/
│   ├── docker-compose.yml
│   ├── docker-compose.dev.yml
│   └── nginx/
├── build.gradle
└── README.md
```

## Configuration

- **Firebase:**  
  Place your Firebase service account JSON in `src/main/resources/firebase/firebaseservice.json` for development, or mount it as a secret in production.
- **Database:**  
  Configure credentials in `docker/.env`.

## Useful Commands

- **Start dev environment:**  
  ```sh
  ./gradlew dockerDevUp
  ```
- **Stop dev environment:**  
  ```sh
  ./gradlew dockerDevDown
  ```

## License

MIT

---

**Contributions welcome!**