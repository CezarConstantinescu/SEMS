# SEMS — Social Events Management System

This repository is a full-stack Java application demonstrating JPA-based domain modeling with a Spring Boot REST API backend and Vaadin frontend for managing events, venues, users, and tickets.

**Overview**
- **Language & build:** Java 17, Maven.
- **Backend:** Spring Boot 3.2.2 with REST API controllers.
- **Frontend:** Vaadin 24.2.0 with responsive UI components.
- **JPA:** Jakarta Persistence + Hibernate 6.4.1.
- **Database:** H2 (file-backed at `./data/semsdb`).
- **Architecture:** RESTful service layer with DTOs, frontend REST consumers, Vaadin views with data binding.

**Prerequisites**
- **Java 17** installed and on `PATH`.
- **Maven** (3.x) installed and on `PATH`.

**Quick Start**

1. **Build the project:**
```powershell
mvn clean compile
```

2. **Seed the database with sample data:**
```powershell
mvn exec:java "-Dexec.mainClass=com.sems.demo.SEMSDemo"
```
This creates sample `Venue`, `Concert`, `Wedding`, `User`, and `Ticket` data.

3. **Start the Spring Boot application:**
```powershell
mvn spring-boot:run
```
Watch the console for "Started Application" and Tomcat on port `8080`.

4. **Access the application:**
- **Frontend UI:** http://localhost:8080
- **REST API:** http://localhost:8080/api/events, /api/venues, /api/users, /api/tickets
- **H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:file:./data/semsdb`, user: `sa`, password: blank)

**Architecture**

**Backend (REST API)**
- **Controllers:** `src/main/java/com/sems/controller` - REST endpoints returning DTOs
- **Response DTOs:** `src/main/java/com/sems/dto` - Data transfer objects to avoid lazy initialization issues
- **Repositories:** `src/main/java/com/sems/repository` - Manual EntityManager-based data access
- **Entities:** `src/main/java/com/sems/model` - JPA entities with inheritance (Event → Concert/Wedding)

**Frontend (Vaadin)**
- **Views:** `src/main/java/com/sems/frontend/view` - Vaadin UI components (EventsView, VenuesView, UsersView, TicketsView)
- **REST Consumers:** `src/main/java/com/sems/frontend/service` - OkHttp-based services calling backend API
- **Frontend DTOs:** `src/main/java/com/sems/frontend/dto` - Client-side data models matching backend DTOs
- **Layout:** `MainLayout` with navigation sidebar for all views

**Key Features**
- DTO-based serialization to prevent lazy initialization exceptions
- Grid components with search/filter capabilities
- Responsive navigation between Events, Venues, Users, and Tickets
- Clean separation between backend REST API and frontend consumers

**Run tests**
```powershell
mvn test
```
Tests include entity model integration tests.

**Important files & locations**
- Main Spring Boot class: `src/main/java/com/sems/Application.java`
- Demo data seeder: `src/main/java/com/sems/demo/SEMSDemo.java`
- Entity model: `src/main/java/com/sems/model` (Event, Concert, Wedding, Ticket, User, Venue)
- REST controllers: `src/main/java/com/sems/controller` (EventController, VenueController, UserController, TicketController)
- Backend DTOs: `src/main/java/com/sems/dto`
- Frontend views: `src/main/java/com/sems/frontend/view`
- Frontend services: `src/main/java/com/sems/frontend/service`
- Persistence config: `src/main/resources/META-INF/persistence.xml` (for demo seeder) and `src/main/resources/application.properties` (for Spring Boot)

**Database Configuration**
- The H2 database is file-backed at `./data/semsdb.mv.db`
- Schema auto-updates via `hibernate.hbm2ddl.auto=update`
- Both the demo seeder and Spring Boot share the same database file
- To reset the database, delete `./data/semsdb.mv.db` and re-run the seeder

**Technology Stack**
- Spring Boot 3.2.2 (Web, Data JPA, Actuator)
- Vaadin 24.2.0 (UI framework)
- Hibernate 6.4.1.Final (ORM)
- H2 2.2.224 (Database)
- OkHttp 4.11.0 (REST client)
- Jackson (JSON serialization)
- JUnit 5 (Testing)

**Notes**
- Controllers return DTOs instead of entities to avoid lazy initialization issues during JSON serialization
- Frontend uses OkHttpClient to consume REST API endpoints
- Vaadin views use `Grid` components with `ListDataProvider` for data binding and filtering
- Event inheritance uses `JOINED` strategy with separate tables for Concert and Wedding
- The legacy `persistence.xml` is retained for the demo seeder; Spring Boot uses `application.properties`
