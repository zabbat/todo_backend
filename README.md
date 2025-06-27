# Todo Backend

A simple Todo backend built with **Spring Boot** (Kotlin), secured with Firebase, and containerized with **Docker**.
This backend can be run with the android app at [https://github.com/zabbat/multi_todo](https://github.com/zabbat/multi_todo).
The project can be run with an Android emulator on the host to speed up backend and Android development.

This used VS Code with kotlin to learn about the spring boot + kotlin capabilities of VS Code. Normally for kotlin Intellij would be used.


## Features

- RESTful API for managing todos
- JWT authentication via Firebase
- PostgreSQL for persistent storage
- pgAdmin on Development environemnt
- Docker Compose for easy local and production setup
- Android Emulator access on host machine
- MockK and AssertJ for expressive Kotlin testing


## Architecture

```text
┌──────────────┐   Firebase ID Token   ┌─────────────────────┐
│  Android App │ ───────────────────▶ │  AuthController      │
└──────────────┘                      │  - validates token   │
                                      │  - issues JWT        │
                                      └─────────┬───────────┘
                                                │  JWT (Bearer)
┌─────────────────────────┐       ┌─────────────▼────────────┐
│ TodoController (REST)   │◀──────│   Spring Security Filter │
│  /api/todos/**          │       └─────────┬───────────────┘
└─────────────────────────┘                 │
                                            ▼
                                     ┌───────────────┐
                                     │  R2DBC Repo   │
                                     └──────┬────────┘
                                            ▼
                                      ┌─────────────┐
                                      │ PostgreSQL  │
                                      └─────────────┘
```

---


## Prerequisites

- [Java 21+](https://adoptopenjdk.net/)
- [Docker](https://www.docker.com/get-started) & [Docker Compose](https://docs.docker.com/compose/)
- (Optional) [Gradle](https://gradle.org/) for local builds
- This proejct was build with VS Code, but with some tweaks it could probably work for intelliJ as well

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

You will also need your own firebase project file in: 
```sh
src/main/resources/firebase/firebaseservice.json
```

See src/main/resources/firebase/firebaseservice.json.example

### 3. Build and Run with Docker Compose

Development:
```sh
./gradlew dockerDevUp
./gradlew dockerDevDown   
```
You can clear the database: 
```sh
./gradlew cleanDevDb  
```

The backend will be available at [https://localhost:443](https://localhost:443) (proxied by NGINX).
pgAdmin will be available at [https://localhost:8088](https://localhost:8088) 


Production:
Development:
```sh
./gradlew dockerProdUp
./gradlew dockerProdDown   
```

The backend will be available at [https://localhost:443](https://localhost:443) (proxied by NGINX).

### 4. Running Locally (without Docker)

```sh
./gradlew bootRunDev
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
## Certs

For development certs are in docker/nginx/certs
You wil have to generate certs. For deployment, you will probably not have nginx at all, or you will need a non selfsigned cert.
Script to do this automatically will be added soon.

To generate cert:

```
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout todo.key -out todo.crt -config openssl.cnf -extensions req_ext
```
To create the Android cert:
```
openssl x509 -in todo.crt -outform DER -out todocert.cer
```
todocert.cer can then be used in the Android project like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <debug-overrides>
        <trust-anchors>
            <certificates src="@raw/todocert"/>
        </trust-anchors>
    </debug-overrides>
</network-security-config>
```

## License

MIT

---

**Contributions welcome!**
