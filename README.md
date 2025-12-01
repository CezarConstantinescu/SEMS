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

**Seed the database (two options)**
- Option A — Run the demo runner (quick, recommended):

```powershell
mvn exec:java -Dexec.mainClass="com.sems.demo.SEMSDemo"
```

This uses the project's JPA utilities to create sample `Venue`, `Event`, `User` and `Ticket` data. If both the demo runner and Spring Boot are pointed at the same H2 file URL, the demo's data will be visible to the web app.

- Option B — Seed on Spring Boot startup: add a `CommandLineRunner` or POST endpoints (not included by default). If you want, I can add a startup data loader for you.

**Start the Spring Boot web app**
- **Command:**

```powershell
mvn spring-boot:run
```

- Watch the console for "Started Application" and Tomcat on port `8080`.

**REST API (read-only endpoints shipped by default)**
- `GET /api/events` — list events
- `GET /api/users` — list users
- `GET /api/venues` — list venues
- `GET /api/tickets` — list tickets

Example PowerShell query:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/events -Method Get
```

If you see an empty array, either the database has no rows, or `SEMSDemo` was not run against the same H2 file.

**H2 Console**
- If enabled in `application.properties`, access the H2 web console at `http://localhost:8080/h2-console` to inspect tables and run SQL queries. Verify the JDBC URL and credentials from `application.properties` or `persistence.xml`.

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

**Gotchas & notes for developers**
- DB sharing: The project still contains `persistence.xml` and also Spring Boot `application.properties`. Ensure both point to the same H2 JDBC URL if you want `SEMSDemo` data visible to the Spring Boot app.
- Transactions: repository methods currently manage transactions; consider converting to Spring Data `JpaRepository` + `@Transactional` service methods for idiomatic Spring behavior.
- Controllers are read-only by default. To create/update data over HTTP, add POST/PUT endpoints or a startup `CommandLineRunner` that seeds sample data.
- JSON serialization: entities include Jackson annotations to reduce lazy-loading/cycle issues when returning entities from controllers.

**Next steps you might want me to do**
- Add a `CommandLineRunner` to seed sample data on Spring Boot startup.
- Add REST POST endpoints for creating `Event` / `User` / `Venue` / `Ticket` resources.
- Migrate repositories to Spring Data `JpaRepository` and use `@Transactional` on service layers.

If you'd like, tell me which of the next steps to take and I will implement it and show live requests against the running app.
