# SEMS — Social Events Management System

This repository is a small Java/Maven application demonstrating JPA-based domain modeling for events, venues, users and tickets. The project includes a legacy demo runner and a Spring Boot web layer with REST endpoints.

**Overview**
- **Language & build:** Java 17, Maven.
- **JPA:** Jakarta Persistence + Hibernate.
- **Database:** H2 (file-backed in this workspace by default).
- **Run modes:** Legacy demo runner (`SEMSDemo`) and Spring Boot app (`Application`).

**Prerequisites**
- **Java 17** installed and on `PATH`.
- **Maven** (3.x) installed and on `PATH`.

**Build**
- **Command:** compile or package the project from the repository root:

```powershell
mvn compile
mvn package
```

**Seed the database**

```powershell
mvn exec:java
```

This uses the project's JPA utilities to create sample `Venue`, `Event`, `User` and `Ticket` data. If both the demo runner and Spring Boot are pointed at the same H2 file URL, the demo's data will be visible to the web app.

```powershell
mvn spring-boot:run
```

- Watch the console for "Started Application" and Tomcat on port `8080`.

**Run tests**
- **Command:**

```powershell
mvn test
```

Tests include unit and integration tests (the project contains a REST integration test using `TestRestTemplate`).

**Important files & locations**
- Main Spring Boot class: `src/main/java/com/sems/Application.java`
- Demo runner: `src/main/java/com/sems/demo/SEMSDemo.java`
- Entities: `src/main/java/com/sems/model` (Event, Ticket, User, Venue, Concert, Wedding)
- Persistence util: `src/main/java/com/sems/persistence/EntityManagerUtil.java`
- Controllers (REST): `src/main/java/com/sems/controller`
- Persistence config: `src/main/resources/META-INF/persistence.xml` and `src/main/resources/application.properties`
- Templates (static samples): `src/main/resources/templates`

**Notes**
- DB sharing: The project still contains `persistence.xml` and also Spring Boot `application.properties`. Ensure both point to the same H2 JDBC URL if you want `SEMSDemo` data visible to the Spring Boot app.
- Transactions: repository methods currently manage transactions; consider converting to Spring Data `JpaRepository` + `@Transactional` service methods for idiomatic Spring behavior.
- Controllers are read-only by default. To create/update data over HTTP, add POST/PUT endpoints or a startup `CommandLineRunner` that seeds sample data.
- JSON serialization: entities include Jackson annotations to reduce lazy-loading/cycle issues when returning entities from controllers.
