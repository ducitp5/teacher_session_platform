# Demo Data Seeder Manual

This document explains how to populate the demonstration data (sessions, users, enrollments) into the application database.

The `DatabaseSeeder` is located in the `com.teachersession.database.seed` package.

## 1. Run via HTTP Controller
Once the application is running, you can trigger the data seeding process by sending an HTTP POST request to the `/api/seeder/run` endpoint.

Using `curl`:
```bash
curl -X POST http://localhost:8080/api/seeder/run
```

## 2. Run via @SpringBootTest
The seeder is integrated with the Spring Boot test context. A test implementation named `DatabaseSeederTest` is available in the test suite.

Using Maven:
```bash
mvn test -Dtest=DatabaseSeederTest
```

## 3. Run via Spring Boot CLI
The seeder uses a `CommandLineRunner` which triggers automatically during startup if the `app.seed.run` property is set to `true`.

Using Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--app.seed.run=true"
```

Using a packaged JAR:
```bash
java -jar target/teacher-session-service-0.0.1-SNAPSHOT.jar --app.seed.run=true
```
